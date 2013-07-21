package org.rembx.android.sfquizz;

import android.content.Context;
import com.google.inject.Inject;
import org.rembx.android.sfquizz.model.GameState;
import org.rembx.android.sfquizz.model.QuizzItem;
import org.rembx.android.sfquizz.model.UsageStatistics;
import org.rembx.android.sfquizz.persistence.PersistenceManager;
import roboguice.inject.ContextSingleton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Quizz Manager
 *
 * @author remibantos
 */
@ContextSingleton
public class QuizzManager {

    Context context;

    PersistenceManager persistenceManager;

    @Inject
    public QuizzManager(Context context, PersistenceManager persistenceManager) {
        this.context = context;
        this.persistenceManager = persistenceManager;
    }

    private List<QuizzItem> remainingItems;

    private QuizzItem current;

    private int goodAnswers;
    private int totalAnswers;
    private int totalAnswered;

    public void newQuizz() {
        remainingItems = new ArrayList<QuizzItem>();

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(context.getAssets()
                    .open(context.getResources().getString(R.string.questionsDS))));
            String questLine;
            while ((questLine = br.readLine()) != null) {
                QuizzItem quest = quizzItemFromCSVLine(questLine);
                remainingItems.add(quest);
            }
        } catch (IOException e) {
            throw new IllegalStateException("error while loading quizz", e);
        }
        randomizeRemainingItems();

        totalAnswers = remainingItems.size();
        GameState gameState = new GameState();
        gameState.setTotalAnswers(totalAnswers);
        persistenceManager.persist(gameState);
        if (persistenceManager.getItem(UsageStatistics.class) == null){
            persistenceManager.persist(new UsageStatistics());
        }

    }

    public void resumeQuizz() {
        GameState gameState = persistenceManager.getItem(GameState.class);
        try {
            loadRemainingItemsByIds(gameState.getRemainingItemIds());
            randomizeRemainingItems();
            totalAnswers = gameState.getTotalAnswers();
            totalAnswered = gameState.getTotalAnswered();
            goodAnswers = gameState.getGoodAnswers();
        } catch (IOException e) {
            throw new IllegalStateException("error while resuming game state", e);
        }
    }

    public boolean isResumable() {
        GameState gameState = persistenceManager.getItem(GameState.class);
        return gameState != null
                && gameState.getRemainingItemIds().size() > 0;
    }

    /**
     * Update statistics and game state if needed
     * update current question with next QuizzItem from remainingItems
     */
    public void loadNextQuestion() {

        GameState gameState = persistenceManager.getItem(GameState.class);
        if (current != null) {
            updateCountersAndUsageStatistics();

            gameState.setRemainingItemIds(remainingItemsIds());
            gameState.setGoodAnswers(goodAnswers);
            gameState.setTotalAnswered(totalAnswered);
            persistenceManager.persist(gameState);
        }

        current = remainingItems.get(0);
        remainingItems.remove(current);

    }

    public void finish() {
        updateCountersAndUsageStatistics();
        persistenceManager.removeItem(GameState.class);
    }

    public UsageStatistics retrieveUsageStatistics() {
        return persistenceManager.getItem(UsageStatistics.class);
    }


    private void randomizeRemainingItems() {
        Collections.shuffle(remainingItems, new Random(System.nanoTime()));
    }

    private void loadRemainingItemsByIds(List<Integer> ids) throws IOException {
        remainingItems = new ArrayList<QuizzItem>();
        BufferedReader br = new BufferedReader(new InputStreamReader(context.getAssets()
                .open(context.getResources().getString(R.string.questionsDS))));
        String questLine;
        while ((questLine = br.readLine()) != null) {
            QuizzItem quizzItem = quizzItemFromCSVLine(questLine);
            if (ids.contains(quizzItem.getId()))
                remainingItems.add(quizzItem);
        }
    }


    private void updateCountersAndUsageStatistics() {
        UsageStatistics usageStatistics = persistenceManager.getItem(UsageStatistics.class);
        if (current.hasBeenCorrectlyAnswered()) {
            goodAnswers++;
            usageStatistics.getGoodAnswered().add(current.getId());
        }
        totalAnswered++;
        usageStatistics.getAnswered().add(current.getId());
        persistenceManager.persist(usageStatistics);
    }

    /**
     * Parse quizz CSV line
     *
     * @param csvLine the csv line to be parsed
     * @return a QuizzItem instance
     */
    private QuizzItem quizzItemFromCSVLine(String csvLine) {
        String[] splited = csvLine.split(";");
        String[] choices = new String[3];
        choices[0] = "  " + splited[2];
        choices[1] = "  " + splited[3];
        choices[2] = "  " + splited[4];
        return new QuizzItem(splited[1], choices, Integer.parseInt(splited[5]) - 1, Integer.parseInt(splited[0]));
    }

    /**
     * @return the current managed QuizzItem
     */
    public QuizzItem getCurrent() {
        return current;
    }

    public int getGoodAnswers() {
        return goodAnswers;
    }

    public int getTotalAnswers() {
        return totalAnswers;
    }


    public int getTotalAnswered() {
        return totalAnswered;
    }

    /**
     * Contains a set of remaining remainingItems (not already answered during the
     * game) Answered remainingItems are removed from this list
     */
    public boolean hasRemainingItems() {
        return (remainingItems != null && remainingItems.size() > 0);
    }

    public List<Integer> remainingItemsIds() {
        List<Integer> remainingIds = new ArrayList<Integer>();
        for (QuizzItem item : remainingItems) {
            remainingIds.add(item.getId());
        }
        return remainingIds;
    }

}

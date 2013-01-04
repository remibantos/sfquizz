package org.rembx.android.sfquizz.helper;

import android.content.Context;
import com.google.inject.Inject;
import org.rembx.android.sfquizz.R;
import org.rembx.android.sfquizz.model.QuizzItem;
import roboguice.inject.ContextSingleton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Quizz Items helper.
 * Load quizz items.
 * Stores current game state
 * @author rembx
 */
@ContextSingleton
public class QuizzItemsHelper {

    Context context;

    @Inject
    public QuizzItemsHelper(Context context) {
        this.context = context;
    }

    /**
     * Contains a set of remaining remainingItems (not already answered during the
     * game) Answered remainingItems are removed from this list
     */
    private List<QuizzItem> remainingItems;


    private List<QuizzItem> allItems;

    /**
     * The current question
     */
    private QuizzItem currentItem;

    private int totalAnswered = 0;
    private int goodAnswersCount = 0;
    private int answersCount = 0;

    public void loadQuizzItems() throws IOException {
        remainingItems = new ArrayList<QuizzItem>();
        allItems = new ArrayList<QuizzItem>();

        BufferedReader br = new BufferedReader(new InputStreamReader(context.getAssets()
                .open(context.getResources().getString(R.string.questionsDS))));
        String questLine;
        while ((questLine = br.readLine()) != null) {
            String[] splited = questLine.split(";");
            String[] choices = new String[3];
            choices[0] = "  " + splited[2];
            choices[1] = "  " + splited[3];
            choices[2] = "  " + splited[4];
            QuizzItem quest = new QuizzItem(splited[1], choices, Integer.parseInt(splited[5]) - 1, Integer.parseInt(splited[0]));
            allItems.add(quest);
            remainingItems.add(quest);
        }
        setAnswersCount(remainingItems.size());

    }


    /**
     * Set current question to a random question from remaining questions set
     */
    public void loadRandomQuestion() {
        setCurrentItem(getRemainingItems().get(choseRandomQuestionIdx()));
    }

    public void popCurrentQuestion() {
        getRemainingItems().remove(getCurrentItem());
    }


    private Integer choseRandomQuestionIdx() {
        return (int) (Math.random() * getRemainingItems().size());
    }


    /**
     * Compute current score
     *
     * @return computed score
     */
    public String computeScore() {
        return context.getResources().getString(R.string.score) + " " + getGoodAnswersCount() + " / " + getAnswersCount();
    }

    public QuizzItem getCurrentItem() {
        return currentItem;
    }

    public void setCurrentItem(QuizzItem currentItem) {
        this.currentItem = currentItem;
    }

    public int getTotalAnswered() {
        return totalAnswered;
    }

    public void setTotalAnswered(int totalAnswered) {
        this.totalAnswered = totalAnswered;
    }

    public int getGoodAnswersCount() {
        return goodAnswersCount;
    }

    public void setGoodAnswersCount(int goodAnswersCount) {
        this.goodAnswersCount = goodAnswersCount;
    }

    public int getAnswersCount() {
        return answersCount;
    }

    public void setAnswersCount(int answersCount) {
        this.answersCount = answersCount;
    }

    public List<QuizzItem> getRemainingItems() {
        return remainingItems;
    }

    public void setRemainingItemsByIds(List<Integer> remainingItemsIds) {
        remainingItems = new ArrayList<QuizzItem>();
        for (int itemId : remainingItemsIds) {
            remainingItems.add(allItems.get(itemId));
        }
    }

    public List<Integer> getRemainingItemsIds() {
        List<Integer> remainingIds = new ArrayList<Integer>();
        for (QuizzItem item : remainingItems) {
            remainingIds.add(item.getIdx());
        }
        return remainingIds;
    }

}

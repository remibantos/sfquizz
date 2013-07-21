package org.rembx.android.sfquizz;

import android.content.Context;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.rembx.android.sfquizz.model.GameState;
import org.rembx.android.sfquizz.model.QuizzItem;
import org.rembx.android.sfquizz.model.UsageStatistics;
import org.rembx.android.sfquizz.persistence.PersistenceManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * QuizzManager Test
 *
 * @author remibantos
 */
public class QuizzManagerTest {

    @Mock
    private Context context;
    @Mock
    private PersistenceManager persistenceManager;

    private static File questionsResoureFile;

    private QuizzManager instance;
    public static final String[] PROPOSED_ANSWERS = new String[]{"answer choice 1", "answer choice 2", "answer choice 3"};

    @BeforeClass
    public static void init() throws URISyntaxException {
        questionsResoureFile = new File(QuizzManagerTest.class.getResource("/questions_en_test.csv").toURI());
    }

    @Before
    public void setup() throws IOException {
        MockitoAnnotations.initMocks(this);
        instance = spy(new QuizzManager(context, persistenceManager));
        doReturn(new FileInputStream(questionsResoureFile)).when(instance).getQuestionDSStream();
    }

    @Test
    public void newQuizz_shouldLoadFiveQuizzItemsAndPersistGameStateAndStats() throws Exception {

        instance.newQuizz();
        int expectedItemsNumber = 5;

        assertEquals(expectedItemsNumber, instance.getTotalAnswers());

        assertNotNull(instance.getRemainingItems());
        Comparator<QuizzItem> comparator = getQuizzItemComparator();

        List<QuizzItem> expected = buildExpectedRemainingItems();
        Collections.sort(expected, comparator);
        Collections.sort(instance.getRemainingItems(), comparator);
        assertEquals(expected, instance.getRemainingItems());

        GameState gameState = new GameState();
        gameState.setTotalAnswers(expectedItemsNumber);
        verify(persistenceManager).persist(gameState);
        verify(persistenceManager).persist(new UsageStatistics());
    }

    @Test
    public void resumeQuizz_shouldLoadThreeLastQuizzItems() throws Exception {
        GameState gameState = new GameState();
        gameState.setRemainingItemIds(Arrays.asList(3, 4, 5));
        gameState.setGoodAnswers(1);
        gameState.setTotalAnswers(5);
        gameState.setTotalAnswered(2);
        when(persistenceManager.getItem(GameState.class)).thenReturn(gameState);

        instance.resumeQuizz();

        assertEquals(5, instance.getTotalAnswers());
        assertEquals(2, instance.getTotalAnswered());
        assertEquals(1, instance.getGoodAnswers());

        assertNotNull(instance.getRemainingItems());
        Comparator<QuizzItem> comparator = getQuizzItemComparator();

        List<QuizzItem> expected = buildExpectedRemainingItemsForResume();
        Collections.sort(expected, comparator);
        Collections.sort(instance.getRemainingItems(), comparator);

        assertEquals(expected, instance.getRemainingItems());

        verify(persistenceManager, times(1)).getItem(GameState.class);
    }

    @Test
    public void testIsResumable() throws Exception {
        GameState gameState = new GameState();
        gameState.setRemainingItemIds(Arrays.asList(3, 4, 5));
        when(persistenceManager.getItem(GameState.class)).thenReturn(gameState);
        assertTrue(instance.isResumable());
        verify(persistenceManager, times(1)).getItem(GameState.class);
    }

    @Test
    public void testLoadNextQuestion_LastAnswerWrong_shouldLoadNextAndUpdateCountersStatsAndGameState() throws Exception {
        when(persistenceManager.getItem(UsageStatistics.class)).thenReturn(new UsageStatistics());

        when(persistenceManager.getItem(GameState.class)).thenReturn(new GameState());

        instance.setRemainingItems(buildExpectedRemainingItemsForResume());

        QuizzItem current = new QuizzItem(null, null, null, 3);

        instance.setCurrent(current);

        instance.loadNextQuestion();

        assertEquals(2, instance.getRemainingItems().size());
        assertEquals(4, instance.getRemainingItems().get(0).getId().intValue());

        UsageStatistics updatedUsageStats = new UsageStatistics();
        updatedUsageStats.getAnswered().add(3);
        GameState updatedGameState = new GameState();
        updatedGameState.setRemainingItemIds(Arrays.asList(3, 4, 5));
        updatedGameState.setGoodAnswers(0);
        updatedGameState.setTotalAnswered(1);
        updatedGameState.setTotalAnswers(0);

        verify(persistenceManager).getItem(UsageStatistics.class);
        verify(persistenceManager).persist(updatedUsageStats);
        verify(persistenceManager).persist(updatedGameState);
    }

    @Test
    public void testLoadNextQuestion_LastQuestionWrong_shouldLoadNextAndUpdateCountersStatsAndGameState() throws Exception {
        when(persistenceManager.getItem(UsageStatistics.class)).thenReturn(new UsageStatistics());

        when(persistenceManager.getItem(GameState.class)).thenReturn(new GameState());

        instance.setRemainingItems(buildExpectedRemainingItemsForResume());

        QuizzItem current = new QuizzItem(null, null, null, 3);
        current.setHasBeenCorrectlyAnswered(true);

        instance.setCurrent(current);

        instance.loadNextQuestion();

        assertEquals(2, instance.getRemainingItems().size());
        assertEquals(4, instance.getRemainingItems().get(0).getId().intValue());

        UsageStatistics updatedUsageStats = new UsageStatistics();
        updatedUsageStats.getAnswered().add(3);
        updatedUsageStats.getGoodAnswered().add(3);
        GameState updatedGameState = new GameState();
        updatedGameState.setRemainingItemIds(Arrays.asList(3, 4, 5));
        updatedGameState.setGoodAnswers(1);
        updatedGameState.setTotalAnswered(1);
        updatedGameState.setTotalAnswers(0);
        verify(persistenceManager).getItem(UsageStatistics.class);
        verify(persistenceManager).persist(updatedUsageStats);
        verify(persistenceManager).persist(updatedGameState);
    }


    @Test
    public void finish_LastAnswersCorrect_shouldResetGameStateUpdateManagerAndUsageStatistics() throws Exception {
        when(persistenceManager.getItem(UsageStatistics.class)).thenReturn(new UsageStatistics());
        QuizzItem current = new QuizzItem(null, null, null, 1);
        current.setHasBeenCorrectlyAnswered(true);
        instance.setCurrent(current);
        instance.finish();
        assertEquals(1, instance.getGoodAnswers());
        assertEquals(1, instance.getTotalAnswered());

        UsageStatistics modifUsageStats = new UsageStatistics();
        modifUsageStats.getAnswered().add(1);
        modifUsageStats.getGoodAnswered().add(1);
        verify(persistenceManager).getItem(UsageStatistics.class);
        verify(persistenceManager).persist(modifUsageStats);
        verify(persistenceManager).removeItem(GameState.class);
    }

    @Test
    public void finish_LastAnswersWrong_shouldResetGameStateUpdateManagerAndUsageStatistics() throws Exception {
        when(persistenceManager.getItem(UsageStatistics.class)).thenReturn(new UsageStatistics());
        QuizzItem current = new QuizzItem(null, null, null, 1);
        instance.setCurrent(current);
        instance.finish();
        assertEquals(1, instance.getTotalAnswered());

        UsageStatistics modifUsageStats = new UsageStatistics();
        modifUsageStats.getAnswered().add(1);
        verify(persistenceManager).getItem(UsageStatistics.class);
        verify(persistenceManager).persist(modifUsageStats);
        verify(persistenceManager).removeItem(GameState.class);
    }

    @Test
    public void testRetrieveUsageStatistics() throws Exception {
        instance.retrieveUsageStatistics();
        verify(persistenceManager).getItem(UsageStatistics.class);
    }

    @Test
    public void hasRemainingItems_NoneEmptyList_shouldReturnTrue() throws Exception {
        instance.setRemainingItems(buildExpectedRemainingItemsForResume());
        assertTrue(instance.hasRemainingItems());
    }

    @Test
    public void hasRemainingItems_EmptyList_shouldReturnFalse() throws Exception {
        assertFalse(instance.hasRemainingItems());
    }

    @Test
    public void testRemainingItemsIds() throws Exception {
        instance.setRemainingItems(buildExpectedRemainingItemsForResume());
        assertEquals(Arrays.asList(3, 4, 5), instance.remainingItemsIds());
    }

    private List<QuizzItem> buildExpectedRemainingItemsForResume() {
        List<QuizzItem> quizzItems = buildExpectedRemainingItems();
        quizzItems.remove(0);
        quizzItems.remove(0);
        return quizzItems;
    }

    private List<QuizzItem> buildExpectedRemainingItems() {
        List<QuizzItem> quizzItems = new ArrayList<QuizzItem>();
        int answerIdxCount = 0;
        for (int i = 0; i < 5; i++) {
            if (answerIdxCount == 3)
                answerIdxCount = 0;
            QuizzItem quizzItem = new QuizzItem("question " + (i + 1) + " text", PROPOSED_ANSWERS,
                    answerIdxCount, i + 1);
            quizzItems.add(quizzItem);
            answerIdxCount++;
        }
        return quizzItems;
    }

    private Comparator<QuizzItem> getQuizzItemComparator() {
        return new Comparator<QuizzItem>() {

            @Override
            public int compare(QuizzItem quizzItem, QuizzItem quizzItem2) {
                return quizzItem.getId() > quizzItem2.getId() ? 1 : -1;
            }
        };
    }

}

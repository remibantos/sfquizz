package org.rembx.android.sfquizz.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.google.inject.Inject;
import org.rembx.android.sfquizz.R;
import org.rembx.android.sfquizz.helper.QuizzItemsHelper;
import org.rembx.android.sfquizz.model.GameState;
import org.rembx.android.sfquizz.model.Stats;
import org.rembx.android.sfquizz.repository.RepositoryItemsCache;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

/**
 * SF Quizz Activity.
 * Use roboguice to inject dependencies.
 *
 * @author rembx
 */
public class QuizzActivity extends RoboActivity {

    @InjectView(R.id.buttonNewGame)
    Button startBtn;
    @InjectView(R.id.buttonResumeGame)
    Button resumeBtn;
    @InjectView(R.id.button03)
    Button statsBtn;
    @InjectView(R.id.button02)
    Button quitBtn;
    @InjectView(R.id.ChoicesRadioGroup)
    RadioGroup choices;
    @InjectView(R.id.Choice1)
    RadioButton choice1;
    @InjectView(R.id.Choice2)
    RadioButton choice2;
    @InjectView(R.id.Choice3)
    RadioButton choice3;
    @InjectView(R.id.mainLayout)
    View mainLayout;
    @InjectView(R.id.footerTable)
    View footer;
    @InjectView(R.id.WelcomeTextView)
    TextView welcomeTextArea;
    @InjectView(R.id.ScoreTextView)
    TextView scoreArea;
    @InjectView(R.id.QuestiontextView)
    TextView questionArea;
    @InjectView(R.id.menuLayout)
    View menuLayout;
    @Inject
    QuizzItemsHelper quizzItemsManager;
    @Inject
    RepositoryItemsCache itemsCache;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        startBtn.setOnClickListener(newGameListener);
        resumeBtn.setOnClickListener(resumeGameListener);
        quitBtn.setOnClickListener(quitButtonListener);
        statsBtn.setOnClickListener(statsButtonListener);

        showResumeGameButton();

        try {
            initGameResources();
        } catch (Exception e) {
            e.printStackTrace();
        }

        choice1.setOnClickListener(choiceListener);
        choice2.setOnClickListener(choiceListener);
        choice3.setOnClickListener(choiceListener);
    }


    @Override
    public void finish() {

        itemsCache.persistItems(Arrays.asList(itemsCache.getItem(GameState.class), itemsCache.getItem(Stats.class)));

        super.finish();
    }


    /**
     * Listener handling new game launch
     */
    private OnClickListener newGameListener = new OnClickListener() {
        public void onClick(View v) {
            handleStartGame(v);
        }
    };

    /**
     * Listener handling game resume
     */
    private OnClickListener resumeGameListener = new OnClickListener() {
        public void onClick(View v) {
            try {
                GameState gameState = itemsCache.getItem(GameState.class);
                quizzItemsManager.setTotalAnswered(quizzItemsManager.getAnswersCount() - gameState.getRemainingItemIds().size() + 1);
                quizzItemsManager.setRemainingItemsByIds(gameState.getRemainingItemIds());
                scoreArea.setText(quizzItemsManager.computeScore());
            } catch (Exception e) {
                e.printStackTrace();
            }
            handleStartGame(v);
        }
    };

    /**
     * Listener for quit button
     */
    private OnClickListener quitButtonListener = new OnClickListener() {
        public void onClick(View v) {
            handleQuitApplication(v);
        }
    };


    /**
     * Listener for remainingQuestions radio select perform stats and game status update.
     */
    private OnClickListener choiceListener = new OnClickListener() {
        public void onClick(View v) {
            handleChooseAnswer((RadioButton) v);
        }
    };


    /**
     * Listener for stats button
     */
    private OnClickListener statsButtonListener = new OnClickListener() {
        public void onClick(View v) {
            handleShowStatistics(v);
        }
    };


    /**
     * Called for resumed or newly started game
     *
     * @param v , the source View of the event handled.
     */
    private void handleStartGame(View v) {

        welcomeTextArea.setVisibility(View.GONE);
        menuLayout.setVisibility(View.GONE);

        Resources res = getResources();
        Drawable drawable = res.getDrawable(R.drawable.bg_recad);
        mainLayout.setBackgroundDrawable(drawable);
        v.setClickable(false);
        footer.setVisibility(View.VISIBLE);

        // Perform action on clicks
        questionArea.setVisibility(View.VISIBLE);
        quizzItemsManager.loadRandomQuestion();
        questionArea.setText(computeCurrentQuizzQuestion());
        refreshCurrentQuestionChoices();
    }

    private void handleQuitApplication(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setMessage(R.string.quit_message).setCancelable(false)
                .setPositiveButton(R.string.quit, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }


    private void handleChooseAnswer(RadioButton rb) {
        quizzItemsManager.setTotalAnswered(quizzItemsManager.getTotalAnswered() + 1);
        alertAnswerChoiceResult(rb);

    }

    private void handleShowStatistics(View v) {
        int allAnswered = 0;
        int goodAnswered = 0;

        try {
            Stats stats = itemsCache.getItem(Stats.class);
            if (stats.getGoodAnsweredItemIds() != null)
                goodAnswered = stats.getGoodAnsweredItemIds().size();
            if (stats.getAnsweredItemIds() != null)
                allAnswered = stats.getAnsweredItemIds().size();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String mess;
        if (allAnswered > 0)
            mess = getResources().getString(R.string.stats1) + " " + goodAnswered + " " + getResources().getString(R.string.stats2) + " "
                    + quizzItemsManager.getAnswersCount() + " " + getResources().getString(R.string.stats3);
        else
            mess = getResources().getString(R.string.stats4);

        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setMessage(mess).setCancelable(false).setTitle(R.string.stats)
                .setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder.show();
    }

    private void alertAnswerChoiceResult(RadioButton rb) {

        String alertMess = "";
        String alertTitle = "    " + getResources().getString(R.string.wrong) + "    ";
        Integer alertIcon = R.drawable.help;
        if (rb.getText().equals(quizzItemsManager.getCurrentItem().getAnswer())) {
            updateGameStateAndStatistics(true);
            alertTitle = "    " + getResources().getString(R.string.correct) + "    ";
            alertIcon = R.drawable.checked;
            quizzItemsManager.setGoodAnswersCount(quizzItemsManager.getGoodAnswersCount() + 1);

        } else {
            updateGameStateAndStatistics(false);
            alertMess = getResources().getString(R.string.wrong_message) + " " + quizzItemsManager.getCurrentItem().getAnswer();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(rb.getContext());
        builder.setTitle(alertTitle).setIcon(alertIcon).setMessage(alertMess).setCancelable(false)
                .setPositiveButton(R.string.next, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        handleNextQuestion();
                        dialog.cancel();
                    }
                });
        builder.show();
    }

    private void showResumeGameButton() {

        GameState previousGameState = itemsCache.getItem(GameState.class);
        if (previousGameState != null && previousGameState.getRemainingItemIds().size() > 0) {
            resumeBtn.setVisibility(View.VISIBLE);
        }
    }

    private void updateGameStateAndStatistics(boolean doAddGoodAnswerIdx) {

        Stats stats = itemsCache.getItem(Stats.class);
        if (doAddGoodAnswerIdx)
            stats.getGoodAnsweredItemIds().add(quizzItemsManager.getCurrentItem().getIdx());
        stats.getAnsweredItemIds().add(quizzItemsManager.getCurrentItem().getIdx());
        GameState gameState = itemsCache.getItem(GameState.class);
        gameState.setRemainingItemIds(quizzItemsManager.getRemainingItemsIds());
        itemsCache.updateItem(stats);
        itemsCache.updateItem(gameState);
    }

    private String computeEndGameMessage() {
        String endMess = getResources().getString(R.string.end_quizz);
        endMess += " " + quizzItemsManager.getGoodAnswersCount();
        endMess += " " + getResources().getString(R.string.stats2);
        endMess += " " + quizzItemsManager.getAnswersCount();
        endMess += " " + getResources().getString(R.string.stats3);
        return endMess;
    }


    private void handleNextQuestion() {
        try {
            quizzItemsManager.popCurrentQuestion();
            scoreArea.setText(quizzItemsManager.computeScore());
            String tvMessage;
            if ((quizzItemsManager.getRemainingItems().size() > 0)) {
                quizzItemsManager.loadRandomQuestion();
                tvMessage = computeCurrentQuizzQuestion();
                refreshCurrentQuestionChoices();
            } else {
                tvMessage = handleEndGame();
            }
            questionArea.setText(tvMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String computeCurrentQuizzQuestion() {
        return quizzItemsManager.getTotalAnswered() + 1 + " -  " + quizzItemsManager.getCurrentItem().getValue() + "\n";
    }

    private String handleEndGame() {
        String tvMessage;
        choices.setVisibility(View.GONE);
        tvMessage = computeEndGameMessage();

        itemsCache.removeItem(itemsCache.getItem(GameState.class));
        return tvMessage;
    }


    /**
     * Init game resources, questions, game state, stats...
     */
    private void initGameResources() throws IOException {
        Stats stats = itemsCache.getItem(Stats.class);
        if (stats == null)
            stats = new Stats(new HashSet<Integer>(), new HashSet<Integer>());
        //System.out.println(this.fileList());
        GameState gameState = itemsCache.getItem(GameState.class);
        if (gameState == null)
            gameState = new GameState(new ArrayList<Integer>());
        itemsCache.updateItem(stats);
        itemsCache.updateItem(gameState);
        quizzItemsManager.loadQuizzItems();
    }


    /**
     * Update quizz item choices view
     */
    private void refreshCurrentQuestionChoices() {
        choices.setVisibility(View.VISIBLE);
        choice1.setText(quizzItemsManager.getCurrentItem().getChoices()[0]);
        choice2.setText(quizzItemsManager.getCurrentItem().getChoices()[1]);
        choice3.setText(quizzItemsManager.getCurrentItem().getChoices()[2]);
        choice1.setChecked(false);
        choice2.setChecked(false);
        choice3.setChecked(false);
    }

}
package org.rembx.android.sfquizz;

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
import org.rembx.android.sfquizz.model.UsageStatistics;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

/**
 * SF Quizz Activity.
 * Use roboguice to inject dependencies.
 *
 * @author remibantos
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
    QuizzManager quizzManager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        startBtn.setOnClickListener(newGameListener);
        resumeBtn.setOnClickListener(resumeGameListener);
        quitBtn.setOnClickListener(quitButtonListener);
        statsBtn.setOnClickListener(statsButtonListener);

        if (quizzManager.isResumable()) {
            resumeBtn.setVisibility(View.VISIBLE);
        }


        initGameResources();


        choice1.setOnClickListener(choiceListener);
        choice2.setOnClickListener(choiceListener);
        choice3.setOnClickListener(choiceListener);
    }


    @Override
    public void finish() {
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
     * Listener handling game resumeQuizz
     */
    private OnClickListener resumeGameListener = new OnClickListener() {
        public void onClick(View v) {
            try {
                quizzManager.resumeQuizz();
                updateScoreArea();
            } catch (Exception e) {
                e.printStackTrace();
            }
            handleStartGame(v);
        }
    };

    private void updateScoreArea() {
        scoreArea.setText(
                getApplicationContext().getResources().getString(R.string.score) + " "
                        + quizzManager.getGoodAnswers() + " / " + quizzManager.getTotalAnswers()
        );
    }

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
            boolean success = (((RadioButton) v).getText().equals(quizzManager.getCurrent().getQuestion()));
            alertAnswerResult(success);
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
        questionArea.setText(quizzManager.getCurrent().getQuestion());

        updatePossibleAnswersArea();
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


    private void handleShowStatistics(View v) {

        UsageStatistics usageStatistics = quizzManager.retrieveUsageStatistics();

        String mess;
        if (usageStatistics.getAnswered().size() > 0)
            mess = getResources().getString(R.string.stats1) + " " + usageStatistics.getGoodAnswered()
                    + " " + getResources().getString(R.string.stats2) + " "
                    + quizzManager.getTotalAnswers() + " " + getResources().getString(R.string.stats3);
        else
            mess = getResources().getString(R.string.stats4);

        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setMessage(mess).setCancelable(false).setTitle(R.string.usageStatistics)
                .setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder.show();
    }

    private void alertAnswerResult(boolean success) {

        String alertMess = "";
        String alertTitle = "    " + getResources().getString(R.string.wrong) + "    ";
        Integer alertIcon = R.drawable.help;

        if (success) {
            alertTitle = "    " + getResources().getString(R.string.correct) + "    ";
            alertIcon = R.drawable.checked;

        } else {
            alertMess = getResources().getString(R.string.wrong_message) + " " + quizzManager.getCurrent().getAnswer();
        }

        final boolean result = success;

        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        builder.setTitle(alertTitle).setIcon(alertIcon).setMessage(alertMess).setCancelable(false)
                .setPositiveButton(R.string.next, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        handleNextQuestion(result);
                        dialog.cancel();
                    }
                });
        builder.show();
    }

    private String computeEndGameMessage() {
        String endMess = getResources().getString(R.string.end_quizz);
        endMess += " " + quizzManager.getGoodAnswers();
        endMess += " " + getResources().getString(R.string.stats2);
        endMess += " " + quizzManager.getTotalAnswers();
        endMess += " " + getResources().getString(R.string.stats3);
        return endMess;
    }


    private void handleNextQuestion(boolean currentResult) {
        try {
            updateScoreArea();
            String tvMessage;
            if ((quizzManager.getRemainingItems().size() > 0)) {
                quizzManager.loadNextQuestion(currentResult);
                tvMessage = quizzManager.getCurrent().getQuestion();
                updatePossibleAnswersArea();
            } else {
                tvMessage = handleEndGame();
            }
            questionArea.setText(tvMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String handleEndGame() {
        String tvMessage;
        choices.setVisibility(View.GONE);
        tvMessage = computeEndGameMessage();
        quizzManager.finish();
        return tvMessage;
    }


    /**
     * Init game resources, questions, game state, stats...
     */
    private void initGameResources() {
        quizzManager.loadQuizz();
    }


    /**
     * Update quizz item choices view
     */
    private void updatePossibleAnswersArea() {
        choices.setVisibility(View.VISIBLE);
        choice1.setText(quizzManager.getCurrent().getProposedAnswers()[0]);
        choice2.setText(quizzManager.getCurrent().getProposedAnswers()[1]);
        choice3.setText(quizzManager.getCurrent().getProposedAnswers()[2]);
        choice1.setChecked(false);
        choice2.setChecked(false);
        choice3.setChecked(false);
    }

}
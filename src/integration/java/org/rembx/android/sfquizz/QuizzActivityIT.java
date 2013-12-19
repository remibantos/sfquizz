package org.rembx.android.sfquizz;

import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import org.android.sfquizz.R;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
public class QuizzActivityIT {

    private QuizzActivity activity;

    private TextView welcomeTextArea;
    private Button newGameButton;
    private Button resumeButton;
    private View startMenuLayout;
    private View footer;
    private View mainLayout;
    private TextView questionArea;
    private RadioGroup choices;
    RadioButton choice1;
    RadioButton choice2;
    RadioButton choice3;


    @Before
    public void setUp() {
        activity = Robolectric.buildActivity(QuizzActivity.class).create().get();
        welcomeTextArea = (TextView) activity.findViewById(R.id.WelcomeTextView);
        newGameButton = (Button) activity.findViewById(R.id.buttonNewGame);
        resumeButton = (Button) activity.findViewById(R.id.buttonResumeGame);
        startMenuLayout=  activity.findViewById(R.id.menuLayout);
        footer=  activity.findViewById(R.id.footerTable);
        mainLayout = activity.findViewById(R.id.mainLayout);
        questionArea = (TextView) activity.findViewById(R.id.QuestiontextView);
        choices = (RadioGroup) activity.findViewById(R.id.ChoicesRadioGroup);
        choice1 = (RadioButton) activity.findViewById(R.id.Choice1);
        choice2 = (RadioButton) activity.findViewById(R.id.Choice2);
        choice3 = (RadioButton) activity.findViewById(R.id.Choice3);

    }

    @Test
    public void shouldStartWithWelcomeMessageAndNewGameButtonVisibleAndResumeButtonInvisible() throws Exception {

        assertThat(welcomeTextArea.getText().toString(),
                equalTo(activity.getText(R.string.welcomeMessage)));
        assertThat(welcomeTextArea.getVisibility(),equalTo(View.VISIBLE));

        assertThat(newGameButton.getVisibility(),
                equalTo(View.VISIBLE));
        assertThat(resumeButton.getVisibility(),
                equalTo(View.GONE));

    }

    @Test
    public void clickNewGameButton_shouldTriggerChangesForQuestionView() throws Exception {

        newGameButton.performClick();

        assertThat(welcomeTextArea.getVisibility(),equalTo(View.GONE));
        assertThat(startMenuLayout.getVisibility(),equalTo(View.GONE));
        assertThat(footer.getVisibility(),equalTo(View.VISIBLE));
        assertThat(mainLayout.getBackground(),equalTo(activity.getResources().getDrawable(R.drawable.bg_recad)));
        assertThat(questionArea.getVisibility(), equalTo(View.VISIBLE));
        assertThat(choices.getVisibility(),equalTo(View.VISIBLE));
        assertThat(choice1.getVisibility(),equalTo(View.VISIBLE));
        assertThat(choice2.getVisibility(),equalTo(View.VISIBLE));
        assertThat(choice3.getVisibility(),equalTo(View.VISIBLE));

    }

    @Test
    public void answerEveryQuestions_shouldReadEndGameScreen() throws Exception {

        newGameButton.performClick();
        List<RadioButton> randomChoices = Arrays.asList(choice1,choice2,choice3);

        //while (!questionArea.getText().toString().contains(activity.getResources().getString(R.string.end_quizz))){

            //AlertDialog alertDialog = ShadowAlertDialog.getLatestAlertDialog();
            //shadowAlertDialog.clickOnItem(1);

       // }
        randomChoices.get(0).setChecked(true);

        System.out.println(questionArea.getText());
        randomChoices.get(0).setChecked(true);
        System.out.println(questionArea.getText());
        randomChoices.get(0).setChecked(true);
        System.out.println(questionArea.getText());

    }

}

package com.team18.studybuddy.studybuddy;

/**
 * Created by Llama on 10/22/2015.
 */
public interface ISetTextInFragment {
    void enableProgess();
    void disableProgess();
    void showBioText(String testToShow);
    void showNameText(String testToShow);
    void showInterestText(String[] testToShow);
    void showCourseText(String[][] testToShow);

}

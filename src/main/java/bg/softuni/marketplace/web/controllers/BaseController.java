package bg.softuni.marketplace.web.controllers;

import lombok.extern.java.Log;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.beans.PropertyEditorSupport;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static bg.softuni.marketplace.web.common.ViewActionPrefix.FORWARD;
import static bg.softuni.marketplace.web.common.ViewActionPrefix.REDIRECT;

@Log
public class BaseController {

    protected BaseController() {
    }

    protected static String redirect(String url) {
        return REDIRECT + URLEncoder.encode(url, StandardCharsets.UTF_8);
    }

    protected static String forward(String url) {
        return FORWARD + URLEncoder.encode(url, StandardCharsets.UTF_8);
    }

    /**
     * Define modification rules for input fields<br>
     * Settings are inherited by extending classes
     * <ul>Rules applied:
     * <li>Trim all form input field texts (remove blank characters from both start and end of the text)</li>
     * <li>Empty texts are NOT treat as null or as "", depending on {@link #isEmptyInputStringsNull} method</li>
     * <li>Prevent above mentioned rules for any fields returned by {@link #getUnmodifiedTextFieldsList} method</li>
     * </ul>
     *
     * @param binder {@link WebDataBinder}
     */
    @InitBinder
    private void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(isEmptyInputStringsNull()));

        preventTextModificationForFields(binder, getUnmodifiedTextFieldsList());
    }

    /**
     * Defines rules for handling of empty texts ("") in form input fields.
     *
     * @return true: convert empty strings to null<br>
     * false: keep empty strings unmodified
     */
    protected boolean isEmptyInputStringsNull() {
        return false;
    }

    /**
     * Prevent text modification (like trimming) of specified input fields,
     * for example "password", "repeatPassword" etc.
     *
     * @param binder              {@link WebDataBinder}
     * @param doNotTrimFieldsList List of field names that should be read without modifications
     */
    private static void preventTextModificationForFields(WebDataBinder binder, List<String> doNotTrimFieldsList) {
        PropertyEditorSupport noTrimPropertyEditor = new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(text);
            }
        };

        doNotTrimFieldsList.forEach(field ->
                binder.registerCustomEditor(String.class, field, noTrimPropertyEditor));
    }

    /**
     * List of text field names that should be read without any modification by this controller's methods
     *
     * @return List of strings (ex. List.of("password", "confirmPassword"))
     */
    protected List<String> getUnmodifiedTextFieldsList() {
        return List.of();
    }
}

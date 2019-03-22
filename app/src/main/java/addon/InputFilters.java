package addon;

import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Created by Dewi Oktaviani on 1/16/2018.
 */

public class InputFilters {
    public void etTextWatcher(final EditText editText, final Integer length){
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable editable) {
                String originalText = editable.toString();
                int originalTextLength = originalText.length();
                int textSelection = editText.getSelectionStart();
                int currentSelection;
                if (length!= null){
                    if (textSelection > (length + 1) ){
                        currentSelection = length + 1;
                    } else {
                        currentSelection = editText.getSelectionStart() ;
                    }
                } else {
                    currentSelection = editText.getSelectionStart() ;
                }

                // Create the filtered text
                StringBuilder sb = new StringBuilder();
                boolean hasChanged = false;
                for (int i = 0; i < originalTextLength; i++) {
                    char currentChar = originalText.charAt(i);
                    if (isAllowed(currentChar)) {
                        if (length != null){
                            if (i < length){
                                sb.append(currentChar);
                            } else {
                                hasChanged = true;
                                if (currentSelection >= i) {
                                    currentSelection--;
                                }
                            }
                        }else {
                            sb.append(currentChar);
                        }
                    } else {
                        hasChanged = true;
                        if (currentSelection >= i) {
                            currentSelection--;
                        }
                    }
                }

                // If we filtered something, update the text and the cursor location
                if (hasChanged) {
                    String newText = sb.toString();
                    editText.setText(newText);
                    editText.setSelection(currentSelection);
                }
            }

            private boolean isAllowed(char c) {
                // TODO: Add the filter logic here
                return Character.isLetterOrDigit(c) || Character.isSpaceChar(c);
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do Nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

        });
    }

    public void etTextWatcherSPChar(final EditText editText, final Integer length, final char[] chars){
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable editable) {
                String originalText = editable.toString();
                int originalTextLength = originalText.length();
                int textSelection = editText.getSelectionStart();
                int currentSelection;
                if (length!= null){
                    if (textSelection > (length + 1) ){
                        currentSelection = length + 1;
                    } else {
                        currentSelection = editText.getSelectionStart() ;
                    }
                } else {
                    currentSelection = editText.getSelectionStart() ;
                }


                // Create the filtered text
                StringBuilder sb = new StringBuilder();
                boolean hasChanged = false;
                for (int i = 0; i < originalTextLength; i++) {
                    char currentChar = originalText.charAt(i);
                    if (isAllowed(currentChar) || isThereChar(chars, currentChar)) {
                        if (length != null){
                            if (i < length){
                                sb.append(currentChar);
                            } else {
                                hasChanged = true;
                                if (currentSelection >= i) {
                                    currentSelection--;
                                }
                            }
                        }else {
                            sb.append(currentChar);
                        }
                    } else {
                        hasChanged = true;
                        if (currentSelection >= i) {
                            currentSelection--;
                        }
                    }
                }

                // If we filtered something, update the text and the cursor location
                if (hasChanged) {
                    String newText = sb.toString();
                    editText.setText(newText);
                    editText.setSelection(currentSelection);
                }
            }

            private boolean isAllowed(char c) {
                // TODO: Add the filter logic here
                return Character.isLetterOrDigit(c) || Character.isSpaceChar(c);
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do Nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

        });
    }

    public void etCapsTextWatcher(final EditText editText, final Integer length){
        editText.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable editable) {
                String originalText = editable.toString();
                int originalTextLength = originalText.length();
                int textSelection = editText.getSelectionStart();
                int currentSelection;
                if (length!= null){
                    if (textSelection > (length + 1) ){
                        currentSelection = length + 1;
                    } else {
                        currentSelection = editText.getSelectionStart() ;
                    }
                } else {
                    currentSelection = editText.getSelectionStart() ;
                }


                // Create the filtered text
                StringBuilder sb = new StringBuilder();
                boolean hasChanged = false;
                for (int i = 0; i < originalTextLength; i++) {
                    char currentChar = originalText.charAt(i);
                    if (isAllowed(currentChar)) {
                        if (length != null){
                            if (i < length){
                                sb.append(currentChar);
                            } else {
                                hasChanged = true;
                                if (currentSelection >= i) {
                                    currentSelection--;
                                }
                            }
                        }else {
                            sb.append(currentChar);
                        }
                    } else {
                        hasChanged = true;
                        if (currentSelection >= i) {
                            currentSelection--;
                        }
                    }
                }

                // If we filtered something, update the text and the cursor location
                if (hasChanged) {
                    String newText = sb.toString();
                    editText.setText(newText);
                    editText.setSelection(currentSelection);
                }
            }

            private boolean isAllowed(char c) {
                // TODO: Add the filter logic here
                return Character.isLetterOrDigit(c) || Character.isSpaceChar(c) ;
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do Nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

        });
    }

    public void etCapsTextWatcherSPChar(final EditText editText, final Integer length, final char[] chars){
        editText.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable editable) {
                String originalText = editable.toString();
                int originalTextLength = originalText.length();
                int textSelection = editText.getSelectionStart();
                int currentSelection;
                if (length!= null){
                    if (textSelection > (length + 1) ){
                        currentSelection = length + 1;
                    } else {
                        currentSelection = editText.getSelectionStart() ;
                    }
                } else {
                    currentSelection = editText.getSelectionStart() ;
                }

                // Create the filtered text
                StringBuilder sb = new StringBuilder();
                boolean hasChanged = false;
//                char[] chars = {'.', ','};
                for (int i = 0; i < originalTextLength; i++) {
                    char currentChar = originalText.charAt(i);
                    if (isAllowed(currentChar) || isThereChar(chars, currentChar)) {
                        if (length != null){
                            if (i < length){
                                sb.append(currentChar);
                            } else {
                                hasChanged = true;
                                if (currentSelection >= i) {
                                    currentSelection--;
                                }
                            }
                        }else {
                            sb.append(currentChar);
                        }
                    } else {
                        hasChanged = true;
                        if (currentSelection >= i) {
                            currentSelection--;
                        }
                    }
                }

                // If we filtered something, update the text and the cursor location
                if (hasChanged) {
                    String newText = sb.toString();
                    editText.setText(newText);
                    editText.setSelection(currentSelection);
                }
            }

            private boolean isAllowed(char c) {
                // TODO: Add the filter logic here
                return Character.isLetterOrDigit(c) || Character.isSpaceChar(c) ;
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do Nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

        });
    }

    public void etCapsTextWatcherNoSpace(final EditText editText, final Integer length, final char[] chars){
        editText.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable editable) {
                String originalText = editable.toString();
                int originalTextLength = originalText.length();
                int textSelection = editText.getSelectionStart();
                int currentSelection;
                if (length!= null){
                    if (textSelection > (length + 1) ){
                        currentSelection = length + 1;
                    } else {
                        currentSelection = editText.getSelectionStart() ;
                    }
                } else {
                    currentSelection = editText.getSelectionStart() ;
                }

                // Create the filtered text
                StringBuilder sb = new StringBuilder();
                boolean hasChanged = false;
//                char[] chars = {'.', ','};
                for (int i = 0; i < originalTextLength; i++) {
                    char currentChar = originalText.charAt(i);
                    if (isAllowed(currentChar, i) || isThereChar(chars, currentChar)) {
                        if (length != null){
                            if (i < length){
                                sb.append(currentChar);
                            } else {
                                hasChanged = true;
                                if (currentSelection >= i) {
                                    currentSelection--;
                                }
                            }
                        }else {
                            sb.append(currentChar);
                        }
                    } else {
                        hasChanged = true;
                        if (currentSelection >= i) {
                            currentSelection--;
                        }
                    }
                }

                // If we filtered something, update the text and the cursor location
                if (hasChanged) {
                    String newText = sb.toString();
                    editText.setText(newText);
                    editText.setSelection(currentSelection);
                }
            }

            private boolean isAllowed(char c, int position) {
                // TODO: Add the filter logic here
                    return Character.isLetterOrDigit(c) ;
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do Nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

        });
    }

    public void etCapsTextWatcherNoSpaceAtFirst(final EditText editText, final Integer length, final char[] chars){
        editText.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        editText.addTextChangedListener(new TextWatcher() {
            int b = 0;
            @Override
            public void afterTextChanged(Editable editable) {
                String originalText = editable.toString();
                int originalTextLength = originalText.length();
                int textSelection = editText.getSelectionStart();
                int currentSelection;
                if (length!= null){
                    if (textSelection > (length + 1) ){
                        currentSelection = length + 1;
                    } else {
                        currentSelection = editText.getSelectionStart() ;
                    }
                } else {
                    currentSelection = editText.getSelectionStart() ;
                }

                // Create the filtered text
                StringBuilder sb = new StringBuilder();
                boolean hasChanged = false;
//                char[] chars = {'.', ','};
                boolean a = false;
                for (int j = 0 ; j < originalTextLength; j++){
                    char currentChar = originalText.charAt(j);
                    if (currentChar == ' '){
                        b++;
                    } else {
                        break;
                    }
                }
                for (int i = 0; i < originalTextLength; i++) {
                    char currentChar = originalText.charAt(i);
                    if (isAllowed(currentChar, i) || isThereChar(chars, currentChar)) {
                        if (length != null){
                            if (i < length){
                                sb.append(currentChar);
                            } else {
                                hasChanged = true;
                                if (currentSelection >= i) {
                                    currentSelection--;
                                }
                            }
                        }else {
                            sb.append(currentChar);
                        }
                    } else {
                        hasChanged = true;
                        if (currentSelection >= i) {
                            currentSelection--;
                        }
                    }
                }

                // If we filtered something, update the text and the cursor location
                if (hasChanged) {
                    String newText = sb.toString();
                    editText.setText(newText);
                    editText.setSelection(currentSelection);
                }
            }

            private boolean isAllowed(char c, int position) {
                // TODO: Add the filter logic here
                if ((position >= 0 && position <= b) &&  Character.isSpaceChar(c)){
                    return false;
                } else {
                    return Character.isLetterOrDigit(c) || Character.isSpaceChar(c) ;
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do Nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

        });
    }

    public void etTextWatcherNoSpaceAtFirst(final EditText editText, final Integer length, final char[] chars){
        editText.addTextChangedListener(new TextWatcher() {
            int b = 0;
            @Override
            public void afterTextChanged(Editable editable) {
                String originalText = editable.toString();
                int originalTextLength = originalText.length();
                int textSelection = editText.getSelectionStart();
                int currentSelection;
                if (length!= null){
                    if (textSelection > (length + 1) ){
                        currentSelection = length + 1;
                    } else {
                        currentSelection = editText.getSelectionStart() ;
                    }
                } else {
                    currentSelection = editText.getSelectionStart() ;
                }

                // Create the filtered text
                StringBuilder sb = new StringBuilder();
                boolean hasChanged = false;
//                char[] chars = {'.', ','};
                boolean a = false;
                for (int j = 0 ; j < originalTextLength; j++){
                    char currentChar = originalText.charAt(j);
                    if (currentChar == ' '){
                        b++;
                    } else {
                        break;
                    }
                }
                for (int i = 0; i < originalTextLength; i++) {
                    char currentChar = originalText.charAt(i);
                    if (isAllowed(currentChar, i) || isThereChar(chars, currentChar)) {
                        if (length != null){
                            if (i < length){
                                sb.append(currentChar);
                            } else {
                                hasChanged = true;
                                if (currentSelection >= i) {
                                    currentSelection--;
                                }
                            }
                        }else {
                            sb.append(currentChar);
                        }
                    } else {
                        hasChanged = true;
                        if (currentSelection >= i) {
                            currentSelection--;
                        }
                    }
                }

                // If we filtered something, update the text and the cursor location
                if (hasChanged) {
                    String newText = sb.toString();
                    editText.setText(newText);
                    editText.setSelection(currentSelection);
                }
            }

            private boolean isAllowed(char c, int position) {
                // TODO: Add the filter logic here
                if ((position >= 0 && position <= b) &&  Character.isSpaceChar(c)){
                    return false;
                } else {
                    return Character.isLetterOrDigit(c) || Character.isSpaceChar(c) ;
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do Nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

        });
    }

    private boolean isThereChar(char[] chaArray, char chr){
        boolean bool = false;
        for(int i=0; i < chaArray.length; i++) {
            if(chr==chaArray[i]){
                bool = true;
            }
        }
        return bool;
    }
}

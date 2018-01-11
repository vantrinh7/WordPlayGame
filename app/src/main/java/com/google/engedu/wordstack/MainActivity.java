/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.wordstack;

import android.content.res.AssetManager;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    private static final int WORD_LENGTH = 5;
    public static final int LIGHT_BLUE = Color.rgb(176, 200, 255);
    public static final int LIGHT_GREEN = Color.rgb(200, 255, 200);
    private ArrayList<String> words = new ArrayList<>();
    private Random random = new Random();
    private StackedLayout stackedLayout;
    private String word1, word2;
    private LinearLayout word1LinearLayout, word2LinearLayout;
    private Stack<LetterTile> placedTiles = new Stack<LetterTile>();;
    private String TAG = "My activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AssetManager assetManager = getAssets();
        try {
            InputStream inputStream = assetManager.open("words.txt");
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            while((line = in.readLine()) != null) {
                String word = line.trim();
                if (word.length() == WORD_LENGTH) {
                    words.add(word);
                }
            }
        } catch (IOException e) {
            Toast toast = Toast.makeText(this, "Could not load dictionary", Toast.LENGTH_LONG);
            toast.show();
        }
        LinearLayout verticalLayout = (LinearLayout) findViewById(R.id.vertical_layout);
        stackedLayout = new StackedLayout(this);

        verticalLayout.addView(stackedLayout, 3);

        word1LinearLayout = (LinearLayout) findViewById(R.id.word1);
        // Switching onTouch listener and onDrag listener here
        //word1LinearLayout.setOnTouchListener(new TouchListener());
        word1LinearLayout.setOnDragListener(new DragListener());

        word2LinearLayout = (LinearLayout) findViewById(R.id.word2);

        // Switching onTouch listener and onDrag listener here
        //word2LinearLayout.setOnTouchListener(new TouchListener());
        word2LinearLayout.setOnDragListener(new DragListener());
        handleStartButton();
    }

    private class TouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN && !stackedLayout.empty()) {
                // Show the tiles in the text views of word 1 and 2
                LetterTile tile = (LetterTile) stackedLayout.peek();
                tile.moveToViewGroup((ViewGroup) v);

                // Keep track of the placed tiles
                placedTiles.push(tile);

                // Once all letters are used, show the correct answer
                if (stackedLayout.empty()) {
                    TextView messageBox = (TextView) findViewById(R.id.message_box);
                    messageBox.setText(word1 + " " + word2);
                }
                return true;
            }
            return false;
        }
    }

    private class DragListener implements View.OnDragListener {

        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    v.setBackgroundColor(LIGHT_BLUE);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DRAG_ENTERED:
                    v.setBackgroundColor(LIGHT_GREEN);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DRAG_EXITED:
                    v.setBackgroundColor(LIGHT_BLUE);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DRAG_ENDED:
                    v.setBackgroundColor(Color.WHITE);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DROP:
                    // Show the tiles in the text views of word 1 and 2
                    LetterTile tile = (LetterTile) event.getLocalState();
                    tile.moveToViewGroup((ViewGroup) v);

                    // Keep track of the placed tiles
                    placedTiles.push(tile);

                    // Once all letters are used, show the correct answer
                    if (stackedLayout.empty()) {
                        TextView messageBox = (TextView) findViewById(R.id.message_box);
                        messageBox.setText(word1 + " " + word2);
                    }
                    return true;
            }
            return false;
        }
    }

    public void handleStartButton() {
        Button button = (Button) findViewById(R.id.start_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onStartGame(view);
            }
        });
    }

    public boolean onStartGame(View view) {
        // Clicking the start button when there's nothing in the stackLayout removes all views
        if (stackedLayout.empty()) {
            word1LinearLayout.removeAllViews();
            word2LinearLayout.removeAllViews();
            stackedLayout.clear();
        }

        TextView messageBox = (TextView) findViewById(R.id.message_box);
        // Get words with random index from 0 to words.size() - 1
        word1 = words.get(random.nextInt(words.size()));
        word2 = words.get(random.nextInt(words.size()));

        // Convert words to character array so as to later pick the letters

        char[] word1Char = word1.toCharArray();
        char[] word2Char = word2.toCharArray();

        int counter1 = 0;
        int counter2 = 0;
        String scrambled = "";


        // Randomly pick either word 1 or 2 to choose letter from, and put letters into a string
        while ((counter1 < WORD_LENGTH) || (counter2 < WORD_LENGTH)){
            // Randomly pick either number 1 or 2
            int randomPick = random.nextInt(2 + 1 ) + 1;

            if ((randomPick == 1) && (counter1 < WORD_LENGTH)) {
                scrambled = scrambled + word1Char[counter1];
                counter1++;
            }
            else if ((randomPick == 2) && (counter2 < WORD_LENGTH)) {
                scrambled = scrambled + word2Char[counter2];
                counter2++;
            }
        }

        char[] scrambledChar = scrambled.toCharArray();
        for (int i = scrambledChar.length - 1; i > -1 ; i--) {
            LetterTile newTile = new LetterTile(getBaseContext(), scrambledChar[i]);
            stackedLayout.push(newTile);
        }

        //messageBox.setText(scrambled);
        return true;
    }

    public boolean onUndo(View view) {
        if (!placedTiles.empty() && !stackedLayout.empty()) {
            LetterTile tile = placedTiles.pop();
             if (tile != null) {
                 tile.moveToViewGroup(stackedLayout);
             }
            return true;
        }
        return false;
    }
}

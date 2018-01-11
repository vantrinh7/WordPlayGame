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

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import java.util.Stack;

public class StackedLayout extends LinearLayout {

    private Stack<View> tiles = new Stack();

    public StackedLayout(Context context) {
        super(context);
    }

    public void push(View tile) {
        View currentView = new View(getContext());

        // If tiles stack is not empty, remove the tile on top of the stack
        if (!tiles.empty()) {
            currentView = peek();
            this.removeView(currentView);
        }
        // Add the tile to be pushed into the stack
        tiles.push(tile);

        // Add to the view
        this.addView(tile);
    }

    public View pop() {
        View popped = new View(getContext());

        // If tile stack is not empty, pop and remove tile on top of the stack
        if (!tiles.empty()) {
            popped = tiles.pop();
            this.removeView(popped);

            // If peeking doesn't return null value, add the peek result to the view
            if (peek() != null) {
                this.addView(peek());
            }
            // Return the popped result
            return popped;
        }
        return null;
    }

    public View peek() {
        // If the tiles stack is not empty, return the one on top of the stack
        if (!tiles.empty()) {
            return tiles.peek();
        }
        return null;
    }

    public boolean empty() {
        return tiles.empty();
    }

    public void clear() {
        /**
         **
         **  YOUR CODE GOES HERE
         **
         **/
    }
}

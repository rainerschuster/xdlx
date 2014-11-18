package com.rainerschuster.sudoku.vaadin;

import static eu.hurion.vaadin.heroku.VaadinForHeroku.forApplication;
import static eu.hurion.vaadin.heroku.VaadinForHeroku.herokuServer;

public class Launcher {

    public static void main(final String[] args) {
        herokuServer(forApplication(SudokuUI.class)).withoutMemcachedSessionManager().start();
    }

}

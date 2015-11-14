package com.alternatecomputing.jsvn.gui;

import com.alternatecomputing.jsvn.command.Commandable;

import javax.swing.*;

/**
 * command generating a custom component
 */
public interface ComponentCommandable extends Commandable {
    JComponent getComponent();
}

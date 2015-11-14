package com.alternatecomputing.jsvn.command;

import com.alternatecomputing.jsvn.gui.ComponentCommandable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * ls: list URL content.
 * usage: ls URL
 *
 */
public class Ls extends Command {
	private static final String COMMAND = "{0} ls {1} {2}";

    public static final String URL = "URL";
	public static final String TARGETS = "TARGETS";
	public static final String STYLE = "STYLE";

	private String url = null;

	public void init(Map args) throws CommandException {
		super.init(args);

		url = (String) args.get(URL);
		if (url == null || "".equals(url.trim())) {
			throw new CommandException("Missing url");
		}

        String style = "";
		if (!"normal".equals(args.get(STYLE))) {
			style = "--" + args.get(STYLE);
		}
		// build the command
		setCommand(MessageFormat.format(COMMAND, getExecutablePath(), url, style));
	}

	public void setUrl(String url) {
		this.url = url;
	}
}

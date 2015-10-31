package com.alternatecomputing.jsvn.gui;

import com.alternatecomputing.jsvn.model.SVNTreeNodeData;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Component;

/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: 15/05/2003
 * Time: 00:29:41
 * To change this template use Options | File Templates.
 */
public class DirectoryBrowserCellRenderer extends DefaultTableCellRenderer {

	/**
	 *
	 * @param table
	 * @param value
	 * @param isSelected
	 * @param hasFocus
	 * @param row
	 * @param column
	 * @return
	 */
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		DirectoryBrowserTableModel model = (DirectoryBrowserTableModel) table.getModel();
		SVNTreeNodeData data = model.getNodeForRow(row);

		// change the text color to reflect the file's status
		component.setForeground(GenericCellRendererHelper.getStatusColor(data));

		switch (column) {
			case 2:
				((JLabel) component).setHorizontalAlignment(JLabel.RIGHT);
				break;
			default:
				((JLabel) component).setHorizontalAlignment(JLabel.LEFT);
				break;
		}
		return component;
	}
}

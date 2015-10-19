package com.alternatecomputing.jsvn.command;

import java.util.Map;

/**
 *	propedit (pedit, pe): Edit property PROPNAME with $EDITOR on targets.
 *	usage: propedit PROPNAME [TARGETS]
 */
public class PropEdit extends Command implements WorkingCopyModifiable {

	public void init(Map args) throws CommandException {
		super.init(args);

	}
}

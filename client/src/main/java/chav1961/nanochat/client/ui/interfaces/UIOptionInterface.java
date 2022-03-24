package chav1961.nanochat.client.ui.interfaces;

import chav1961.purelib.model.interfaces.NodeMetadataOwner;

public interface UIOptionInterface extends NodeMetadataOwner {
	int		SUPPORT_VISIBILITY = (1 << 0);
	int		SUPPORT_ACCESSABILITY = (1 << 1);
	int		SUPPORT_EDITABILITY = (1 << 2);
	int		SUPPORT_LEFT_POPUP_MENU = (1 << 3);
	int		SUPPORT_RIGHT_POPUP_MENU = (1 << 4);
	int		SUPPORT_LEFT_CLICK = (1 << 5);
	int		SUPPORT_RIGHT_CLICK = (1 << 6);
	int		SUPPORT_LEDT_DBLCLICK = (1 << 7);
	int		SUPPORT_RIGHT_DBLCLICK = (1 << 8);
}

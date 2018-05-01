package com.sunday.common.widgets.gridview;

public interface DragGridBaseAdapter {
	/**
	 * 重新排序
	 * @param oldPosition
	 * @param newPosition
	 */
    void reorderItems(int oldPosition, int newPosition);
	
	
	/**
	 * 隐藏item
	 * @param hidePosition
	 */
    void setHideItem(int hidePosition);
	
	/**
	 * ɾ��ĳ��item
	 * @param removePosition
	 */
    void removeItem(int removePosition);
	

}

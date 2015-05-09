package com.gameOfLife.logic;

public interface GameOfLife {
    public void attachView(Object uiObject);

    public void dettachView();

    public boolean isViewPresent();

    public Object getView();
}

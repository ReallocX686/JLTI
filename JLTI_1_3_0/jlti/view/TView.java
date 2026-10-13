package jlti.view;

public interface TView {
    Object[] getArgs();
    boolean getRes();
    int getWidth();
    int getHeight();
    int getX();
    int getY();
    int getFg();
    int getBg();
    String getText();
    int getType();
    void setRes(boolean i);
    int getIds();
    void setIds(int i);
    boolean getOnfMain();
    void setOnfMain(boolean i);
}
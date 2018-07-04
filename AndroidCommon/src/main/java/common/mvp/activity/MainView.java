package common.mvp.activity;

/**
 * Created by Beidouht on 2017/10/13.
 */

public interface MainView<T> {

    void getDataSuccess(T model,int action);

    void getDataFail(String msg,int action);

}

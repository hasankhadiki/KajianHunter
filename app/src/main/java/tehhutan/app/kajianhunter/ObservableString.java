package tehhutan.app.kajianhunter;

import tehhutan.app.kajianhunter.Interface.OnStringChangeListener;

public class ObservableString
{
    private OnStringChangeListener listener;

    private int value;

    public void setOnIntegerChangeListener(OnStringChangeListener listener)
    {
        this.listener = listener;
    }

    public int get()
    {
        return value;
    }

    public void set(int value)
    {
        this.value = value;

        if(listener != null)
        {
            listener.onStringChanged(value);
        }
    }
}


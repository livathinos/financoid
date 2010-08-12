package app.financoid;

import android.os.Bundle;

public interface GenStates {
	public void saveState(Bundle outState);

	public void restoreState(Bundle inState);
}

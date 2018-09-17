/**
 * 
 */
package com.dev.kaizen.restful;

/**
 * @author abiandono
 *
 */
public interface AsyncTaskCompleteListener<T> {
	public void onTaskComplete(T... params);
}

package com.sidekicker.sidekick;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;

import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public
class Helper
{
	private static DefaultHttpClient httpClient = new DefaultHttpClient();

	/*******************************************************************************************************************************/

	public
	interface HttpTaskListener
	{
		void onReturned(String result);
	}

	public static
	class HttpAsyncTask
			extends AsyncTask<String, Void, String>
	{

		private Fragment mFragment;
		private List mPostData;
		private ProgressDialog mDialog;
		private HttpTaskListener mListener;
		private boolean mIsPreviousWorkDone;

		public
		HttpAsyncTask(Fragment fragment, String urls, List postData, HttpTaskListener listener)
		{
			if (mIsPreviousWorkDone)
				assert (!mIsPreviousWorkDone);

			mFragment = fragment;
			mPostData = postData;
			mListener = listener;
			mIsPreviousWorkDone = false;
			this.execute(urls);
		}

		public
		HttpAsyncTask(Fragment fragment, String urls, HttpTaskListener listener)
		{
			this(fragment, urls, null, listener);
		}

		public
		HttpAsyncTask(Fragment fragment, String urls)
		{
			this(fragment, urls, null, null);
		}

		@Override
		protected
		void onPreExecute()
		{
			// TODO: no more progress dialog
			// check if caller fragment is the current fragment in ViewPager
			//			if (getFragmentIndexInPager(mFragment) == mCurViewPagerIndex && mLoadingTitleAndMsg != DIALOG_NOTHING)
			//				mDialog = ProgressDialog.show(mFragment.getActivity(), mLoadingTitleAndMsg[0], mLoadingTitleAndMsg[1], true, false);

			mIsPreviousWorkDone = true;
		}

		@Override
		protected
		String doInBackground(String... urls)
		{
			if (mPostData == null)
				return Helper.httpGET(urls[0]);
			else
				return Helper.httpPOST(urls[0], mPostData);
		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected
		void onPostExecute(String result)
		{
			if (mDialog != null && mDialog.isShowing())
				mDialog.dismiss();
			mIsPreviousWorkDone = false;

			if (mListener != null)
				mListener.onReturned(result);
		}
	}

	/*******************************************************************************************************************************/

	// check is network connected
	public static
	boolean isNetworkConnected(Context context)
	{
		ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm != null)
		{
			NetworkInfo networkInfo = cm.getActiveNetworkInfo();
			return networkInfo != null && networkInfo.isConnected();
		}
		return false;
	}

	/**
	 * Issue a GET request to the server.
	 *
	 * @param url GET address.
	 */
	public static
	String httpGET(String url)
	{
		String result;
		try
		{
			// make GET request to the given URL
			HttpResponse httpResponse = httpClient.execute(new HttpGet(url));

			// receive response as inputStream
			InputStream inputStream = httpResponse.getEntity()
			                                      .getContent();

			// convert inputStream to string
			if (inputStream != null)
			{
				result = convertInputStreamToString(inputStream);
			}
			else
			{
				result = null;
			}
		}
		catch (Exception e)
		{
			result = null;
		}

		return result;
	}

	/**
	 * Issue a POST request to the server.
	 *
	 * @param url    POST address.
	 * @param params request parameters.
	 */
	public static
	String httpPOST(String url, List params)
	{
		HttpPost post = new HttpPost(url);
		try
		{
//			HttpProtocolParams.setUserAgent(httpClient.getParams(), generateUserAgent());

			//送出HTTP request
			post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			//取得HTTP response
			HttpResponse httpResponse = Helper.httpClient.execute(post);

			//檢查狀態碼，200表示OK
			if (httpResponse.getStatusLine()
			                .getStatusCode() == 200)
			{
				//取出回應字串
				String strResult = EntityUtils.toString(httpResponse.getEntity());
				return strResult;
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public static
	String translateHttpSpecialChar(String s)
	{
		return s.replace("&amp;", "&")
		        .replace("&lt;", "<")
		        .replace("&gt;", ">")
		        .replace("&quot;", "\"")
		        .replace("&apos;", "\'");
	}

	private static
	String convertInputStreamToString(InputStream inputStream)
	throws IOException
	{
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		String line, result = "";
		while ((line = bufferedReader.readLine()) != null)
			result += line;

		inputStream.close();
		return result;
	}

	public static
	float clampFloat(final float value, final float minValue, final float maxValue)
	{
		return Math.min(maxValue, Math.max(minValue, value));
	}

	public static
	int clampInt(final int value, final int minValue, final int maxValue)
	{
		return Math.min(maxValue, Math.max(minValue, value));
	}

}
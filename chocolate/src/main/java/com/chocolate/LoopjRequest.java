package com.chocolate;

import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

public abstract class LoopjRequest<Response extends Request.Response> extends Request<Response, RequestParams, RequestHandle> {



}

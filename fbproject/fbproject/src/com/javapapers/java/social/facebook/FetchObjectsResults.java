package com.javapapers.java.social.facebook;

import com.restfb.Facebook;
import com.restfb.types.Page;
import com.restfb.types.User;

public class FetchObjectsResults {
	  @Facebook
	  User me;

	  // If the Facebook property name doesn't match
	  // the Java field name, specify the Facebook field name in the annotation.

	  @Facebook("cocacola")
	  Page page;
	}
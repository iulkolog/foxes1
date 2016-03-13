package ru.yull.loginovayuliya.foxhunting;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.support.v4.app.Fragment;


public class AdFragment extends Fragment /*implements AdListener*/  {
	  @Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
		      Bundle savedInstanceState) {
		    return inflater.inflate(R.layout.adfragment, container, false);
		  }

}


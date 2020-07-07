package com.matsoft.web;

/**
 * An interface of WebURL provides functinality to control the depth of the search as it also shows how deep is
 * the found URL counting from the seed
 */
public interface WebURL {

    String getUrl();

    int getDepth();

}

package com.tericcabrel.authorization.models.enums;

public enum PermissionLoadMode {
  CREATE("create"),
  UPDATE("update");

  String value;

  PermissionLoadMode(String value) {
    this.value = value;
  }
}

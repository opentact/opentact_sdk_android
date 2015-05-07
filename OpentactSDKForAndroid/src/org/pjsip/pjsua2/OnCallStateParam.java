/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 2.0.11
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package org.pjsip.pjsua2;

public class OnCallStateParam {
  private long swigCPtr;
  protected boolean swigCMemOwn;

  protected OnCallStateParam(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(OnCallStateParam obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        pjsua2JNI.delete_OnCallStateParam(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public void setE(SipEvent value) {
    pjsua2JNI.OnCallStateParam_e_set(swigCPtr, this, SipEvent.getCPtr(value), value);
  }

  public SipEvent getE() {
    long cPtr = pjsua2JNI.OnCallStateParam_e_get(swigCPtr, this);
    return (cPtr == 0) ? null : new SipEvent(cPtr, false);
  }

  public OnCallStateParam() {
    this(pjsua2JNI.new_OnCallStateParam(), true);
  }

}
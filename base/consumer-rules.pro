#ViewBinding
-keepclassmembers class * implements androidx.viewbinding.ViewBinding{
  inflate(android.view.LayoutInflater);
  inflate(android.view.LayoutInflater,android.view.ViewGroup,boolean);
}

#BaseUserManager
-keepclassmembernames class com.bonepeople.android.base.manager.BaseUserManager

#ViewBindingRecyclerAdapter
-keepclassmembers class * extends com.bonepeople.android.base.ViewBindingRecyclerAdapter{
 <init>(...);
}
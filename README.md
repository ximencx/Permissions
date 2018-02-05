
## Permissions

## 介绍

这是一款权限申请开源库，采用Fragment中转的原理申请权限。特性如下：
* 简洁，链式调用。
* 轻巧，不依赖其他组件。
* 丰富，工具类中是否包含权限方法，跳转到设置界面方法。



——PermissionsManager  权限申请的入口，支持链式调用。

——PermissionUtils  权限申请工具类，支持跳转到设置界面，判断是否包含权限 。



</br>

### **使用步骤：**

#### 1.添加aar依赖：
```java
compile 'com.github.ximencx:Permissions:0.1
```
#### 2.在Activity中添加如下代码：

```java
        PermissionsManager permissionsManager = new PermissionsManager(activity);
        permissionsManager.setPermissions(permissions)//申请权限
                .setOnPermissionResult(new OnPermissionResult() {//设置回调
                    @Override
                    public void onSuccess() {//已有权限或者申请成功
                        Toast.makeText(activity, "success", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailed(boolean onRationale) {//申请的权限没有全部成功
                        //onRationaletrue:    true(拒绝并勾选不在提示选项)
                        Toast.makeText(activity, "failed===onRationale:::" + onRationale, Toast.LENGTH_SHORT).show();
                    }
                }).request();
```

```java
  //判断是否包含权限
  PermissionUtils.hasPermission(activity, Arrays.asList(permissions));
```

```java
  //跳转设置界面
  PermissionUtils.goSettingActivity(activity,101);

  //跳转后回调
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
     super.onActivityResult(requestCode, resultCode, data);
       if (requestCode==101){
              //跳转设置界面后的回调
       }
  }
```
#### 大功告成~

#### 3.对使用还有疑问的话，可参考Demo代码
[请戳我查看demo代码](https://github.com/ximencx/Permissions/blob/master/sample/src/main/java/com/xdja/permission/MainActivity.java)


### Thanks
- [rxpermission](https://github.com/tbruyelle/RxPermissions)
## License

```
Copyright 2014 Bigkoo
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

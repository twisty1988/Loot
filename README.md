# Loot
![Loot Hoarder](art/Loot_Hoarder.png)

Multilpe and Single ImageChoice Library

提供多选和单选图片以及图片裁剪的功能.

<img src="art/device-2017-05-17-155710.png" width="300px"/>
<img src="art/device-2017-05-17-160938.png" width="300px"/>
<img src="art/device-2017-05-17-160901.png" width="300px"/>
<img src="art/device-2017-05-17-160832.png" width="300px"/>
<img src="art/device-2017-05-17-162150.png" width="300px"/>

```
Loot.getInstance()
    .setSingle(false)
    .setHasCamera(true)
    .setHasCrop(false)
    .setMaxCount(9)
    .start(this, data -> {});
```

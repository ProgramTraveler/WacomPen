# ---

## 更新日志

### 2020-11-28

#### 环境的配置

* 今天配完了编译的环境和一些参考代码的阅读
* 配置：32位的eclipse的安装的环境的配置和写字板环境的配置
* 仓库建立

---

### 2020-11-29

#### 使用GUI对初始界面的设计的完成，比较粗糙

* 选择菜单的方向
* 选择完毕的开始按钮
* 界面有待完善...

---

### 2020-11-30

#### 确认笔的数据和文件的写入

* 确认笔的压力，笔的角度，笔的旋转角......
* 能够将笔的一些信息写入创建的csv文件中
* 测试写入的结果是否正确
* 未知信息的考量

---

### 2020-12-05

#### 界面的跟新和一些操作数据测试

* 重写界面，在选择开始按钮后显示写字面板
* 使用cello.tablet.JTabletCursor包来读出笔的各个属性值，目前以笔的压力为测试，后续完善
* 写字板的简单实现，测试为主
* 添加鼠标的监听，在鼠标点击后记录，在鼠标拖动时给出反应
* 后续需要在鼠标拖动时显示线条

---

### 2020-12-06

#### 写字面板的编写和遇到的Bug

* 按下开始按钮后打开写字板
* 打开写字板后拖动鼠标显示线条 **->** **Bug：在写这部分的时候由于没有将初始点进行更新，所以画出的线条是从一个点向外散射的线条,而使用`Graphics`画出的又是一个个小点，所以用`Graphics2D`（已解决）**
* 使用写字板进行一些简单的测试 **->** **Bug：以压力为测试条件，但无法从写字板读出笔的压力值（已解决）**
* 正常获取压力值 **->** **应该是使用`PenValue`里面的`Pressure`里面的函数**
* 能够将每次测试的笔尖压力记录在相应的文件中

---

### 2020-12-07

#### 在写字面板的操作和Bug

* 打开写字板，画一条线 **->** **Bug：当开始画线后会出现开始按钮**（已解决）

---

### 2020-12-09

#### 对与界面新要求

* 在初始界面需要有用户ID和组数Block的数据输入
* 需要有一个选择练习按钮
* 需要在界面中新增选择模式按钮
* 开始按钮 **->** 按照初始设计不变，但需要调整位置

#### 对写字板的新要求

* 写字界面分为传统写字板界面和优化后的写字界面

##### 传统写字板界面要求

* 在传统写字板中加入颜色选择和粗细选择按钮
* 最好能显示测试者当前的颜色和粗细以及应该选择的颜色和粗细
* 在写字板中需要加入两个颜色区域，用于提醒测试者在该区域进行颜色选择和笔的粗细选择
* 记录测试者在做颜色选择和粗细选择中的所花的时间

##### 还有三个与传统界面的做对比的写字界面

* 待讨论......

---

### 2020-12-18

#### 重新对用户初始的登陆页面进行设计

* 新加**用户ID**的输入框
* 新加**实验组数**的输入框
* 新加**练习**选择框
* 但界面的排列还有待改善，目前只考虑方法的实现

#### 完善笔的信息记录

* 获取笔的角度
* 获取笔的旋转角

#### 界面逻辑需要完善

* 当用户选择**开始**后，登录界面需要被写字板覆盖

#### 传统写字板类的创建

* 新添加一个传统写字板的类，用于实现相应功能

---

### 2020-12-19

#### 对登录界面修改

* 解决**2020-12-07**时画线会出现开始按钮的Bug
* 当用户选择开始按钮后，登录界面被写字界面覆盖
* 将四个写字模式的按钮添加到界面

#### 传统写字界面

* 添加两个菜单栏，分别是颜色和像素
* 添加颜色和像素的下拉菜单
* 画线的逻辑需要修改

---

### 2020-12-20

#### 传统写字界面的实现

* 通过用户选择下拉菜单栏，来实现对画笔颜色的选择 **->** **Bug:一旦切换为某个颜色，所有之前的线条都会变成当前选择的颜色**
* 像素中下拉菜单中对画笔的修改暂未实现

#### 点类

* 为了方便对点进行信息处理，建立点类，**也是为了解决切换颜色后所有线条会变为当前颜色的Bug**
* 可以保存当前点的信息
* 通过读取点的位置来画线 **->** **Bug:会出现当重画一条线时，新的线会和旧线连接（已解决）**
* 对点的颜色逻辑处理需要修改，可以做到对线条颜色进行实时处理

---

### 2020-12-21

#### 传统写字界面部分实现

* 通过下拉菜单的形式来对画笔的颜色和粗细进行选择的功能正常实现
* 在写字界面加入两个实验区域，一个为颜色测试区域，一个为像素测试区域
* 当线条进入颜色测试区域时，会提示待选颜色
* 像素测试区域暂未实现
* 颜色测试区域 **->** **Bug:当画笔进入到测试区域的时候会出现提示颜色乱跳的情况** **->** **分析原因：可能是判断逻辑里有问题，到时候再测试一下** **->** **问题确认：随机数生成位置不对**
* 提示颜色乱跳问题 **->** **解决办法：将随机数作为属性放入到点类中，当发现点进入到颜色测试区域，会获得该点当时被赋得随机数，作为颜色提示的依据**

---

### 2020-12-22

#### 传统写字界面更新

* 开始对像素测试区域进行实验，发现原始逻辑存在问题，初始的解决办法不太适用，开始尝试使用手写一个对测试区域的监听来实现对进入测试区域的对应操作，同时，也为了提示方法的优化
* **问题：** 当界面重绘时，发现有些组件不能在重绘界面出现 **->** **解决办法，将原来的界面分割为两个区域，分别进行不同的操作**

---

### 2020-12-23

#### 传统写字界面完成

* 设置在左边界面的中提示标签位置
* 写监听，按照触发事件提示相应动作
* 监听用户当前的笔的颜色和像素，当用户切换颜色和像素时，也跟着变化
* 后面就该写进入测试区域的监听了 **->** **当用户进入颜色测试区域时，在左边的提示区域会显示应该切换的颜色，同理，当进入像素测试区域时，会在左边提示应该切换的像素**
* 对测试区域的监听不能使用现成的监听，还是得自己写一个对应的监听，这样的话，传统写字界面的功能就大致写完了
* 不用监听，直接可以使用鼠标滑动的那个监听器
* 传统界面大致完成

#### 新增要求--->登陆界面标签修改

* 将 **四个写字板** 改为 **四个模式**，细化 **除传统写字板外另外三个模式，比如实列化 -> 实列化模式,PAT实列化**

#### 新增要求--->传统写字界面修改

* 将测试区域修改为长条形
* 去掉默认的颜色和像素在选择菜单中，保证三个颜色对应三个像素
* 优化颜色提示和像素提示
* 数值记录

---

### 2020-12-31

#### 修改传统写字界面

* 修改便签的名称
* 修改测试区域 **->** 将测试区域修改为长条形
* 修改颜色初始的选择菜单，将选择的黑色去掉，将画笔的颜色默认设为黑色
* 修改像素初始的选择菜单，将画笔的初始像素设为1个像素
* 明确模式中所要记录的数据
* 在传统界面的提示区域优化工作未完成

---

### 2021-01-01

#### 左边的界面提示信息美化

* 左边区域中提示信息以文本框的形式出现，而不是纯文字
* 左边区域的显示情况待讨论，暂时先以这样显示
* 开始尝试将数据保存在`csv`文件中，可能会花的时间有点久，因为要记录的数据比较多

---

### 2021-01-02

### 传统写字界面的修改

* 对于用户的提示信息需要修改，到时候确定后再进行修改，不然重复修改太麻烦了

### 信息的记录

* 将信息记录，但要保存的信息需要重新选择
* 按照要求记录信息 **出现的问题->** **文字记录在文件中变成乱码** **->** **解决办法：修改格式，将其改为`GBK`**
* 但文字写入的格式需要修改，在每个列标题中加入`","`作为`csv`文档的分隔符
* **Bug:** **->** **最后一个方位角无法记录在文件中** **->** **解决办法：把这个数据记录两次**

---

### 2021-01-03

#### 信息记录

* 能够直接获取的量已经可以正常记录
* 但其他的实验数据的保存还要在代码中添加
* 当用户进入到任意区域内表示开始测试，这时候开始记录实验数据

#### 写字界面测试区域修改

* 减小两个颜色测试区域之间的间隔

#### 写字界面的键盘监听

* 当按下一个键后，清空界面，开始下一次测试
* **Bug：** **->** **键盘监听无响应** **解决办法：除了要使用`addKeyListener`，还要让其成为`焦点`**

---

### 2021-01-04

#### 绘画时间信息记录

* 信息记录存在问题，笔的间隔太短时会出现0秒的情况，而且时间上的算数运算也存在问题
* 将结果保留两位小数就可以得到正确的结果 **原因** **->** **在计算`/`运算的时候，结果自动保留为整数，而且是向下取整，这就导致了精度的丢失，而产生较大的误差**

---

### 2021-01-06

#### 目标颜色和目标像素的信息记录

* 将目标颜色的信息和目标像素的信息记录在csv文件中
* **注意：** 信息要在进入区域后就开始记录，否则当笔迹出了测试区域后，提示信息会变为空，如果在最后记录的话就只有一个空的字符串

#### 错误切换次数

* 在实验结束后目标颜色与实际颜色做比较，目标像素与实际像素做比较，记录不同的次数

#### 误触发错误

* 当还没有进入测试区域时，用户就开始切换颜色或者像素此时，误触发数加一

#### 一组实验的操作次数

* 当用户结束一次操作后，次数加一

---

### 2021-01-07

#### 返回界面设置

* 做完所有组后返回登陆界面
* **Bug** **->** 但是在设置关闭当前的画线界面，再打开新的登陆界面会出现两个登陆界面，一个是之前的，一个是新打开的
* **Bug** **->** 当使用新的界面登录后会出现上一次的信息还保留在这一次的实验中
* 这个做完所有的次数重新返回到实验的登录界面感觉要花挺长时间的，因为自己写的还是不如意

---

### 2021-01-10

#### 登录界面选择模式的修改

* 传统界面的样式不变
* 其他三个模式细分为`P-T-A`的不同选择模式
* 不同模式中记录的数值还是一样的
* 对原始的登录界面的每个插件的位置还要重新设置
* **Bug** **->** 当将所有模式的选择按钮都在登录界面布局好了之后，除了传统模式，其它在同一排的模式均是倾斜 **->** **解决办法：由于采用的是绝对布局，只能自己把setBound的四个参数慢慢调**

#### 传统写字界面的提示信息样式的修改

* 将原先的两个分离界面去掉，整合为一个界面
* 当前颜色和像素：将当前的颜色还是按照颜色块显示，像素以文字框的形式出现
* 提示切换颜色和像素：提示颜色和像素均以弹窗的样式出现 **->** 看看弹窗的有那些形式，之前也写过一个弹窗，但是会有异常（只是简单的测试一下）
* 将原来的两个测试区域合为一个测试区域，但任要做出两次的提示信息
* 这样的修改相当于把传统界面重新写一次
* 改着改着出现一堆Bug，明天再改

---

### 2021-01-11

#### 颜色提示和像素提示

* 当用户的绘制进入到颜色和像素测试区域时，以弹窗的形式提示用户应该切换的颜色和像素，但需要注意的是，当弹窗出现时，用户的绘制会被打断，此时系统不会判定为鼠标抬起，所以需要在弹窗出现时记录当前线条的绘制时间

#### 颜色和像素的测试

* 一直都在想怎么让颜色和像素能够组合的出现，一种颜色对应是三个像素，而且每一组中都必须将所有的条件都测试一遍，我一直没有好的想法，所以留到现在才开始写
* **现想法：** 将所有的颜色和像素按对应情况分别放入一个颜色集合和像素集合，当每次测试的时候，将集合中的颜色和像素取出，显示给用户，并且将已经显示过的颜色和像素移除，直到集合为空，**缺点：** 所有的颜色和像素只能按顺序出现

#### 定义一个新的类-CompleteExperiment

* 用来提供一次完整实验的实验所需要的信息和变量

#### 传统界面

* 传统界面的完善在今天已经完成了，但还是有一些小的 **Bug** 需要修改一下 :smile:

---

### 2021-01-12

#### 登录界面的底层逻辑修改

* 为了解决登录界面的一些小的问题，对登录的代码进行一些修改
* 对登录界面的排版和按钮位置做出一些修改 **->** 将ID，组数，选择的模式等一些组件采用组合的方式组装到面板中，比原来的情况更好查看
* 登录界面的优化完成

---

### 2021-01-13

#### 开始对传统面板的逻辑进行优化

* 对数据的记录和初始化的问题需要解决
* 传统面板的界面优化 **->** 尽可能美观一点 **->** 选择一个好一点的布局
* 可以参照一下登录界面的布局
* 传统界面的整体结构完成，后面细化一下就可以了
* 标签排版 **->** 为了让界面更贴近PPT的样式，只能自己采用没有布局，采用坐标来精确排版了
* 但有一个问题就是把绿色区域合并了，那就是一进入测试区域就开始弹窗，还是缓冲后就开始弹窗

#### 对传统界面的组数判定问题

* 当测试用户输入完组数时，应该把当前的组数做为后续的是否完成所有组数的测试条件，但目前只是把条件记录，并不能使用当前的信息
* **解决办法->** 当打开用户选择的模式时，将组数作为参数传入到选择的模式中，这样就可以在选择的模式中，使用该参数

#### 当打开新的登录界面，原始数据还有存留的问题

* 当用户把所选的测试组数进行完，重新登录进行新一轮测试时，有的在上一轮的测试数据还保存在当前这一轮数据中，比如进行的实验组数，当上一轮的测试结束后，下一轮的实验组数是在上一轮进行的实验组数的条件下，继续进行 `+ 1`操作

* **解决办法->** 当重新进行登录时，对相关数据进行初始化操作
* 传统界面全部完成，明天开始写实列化模式，冲冲冲 :laughing:

---

### 2021-01-14

#### 开始着手对压力实列化界面的书写

* 创建压力实列化类

#### 对传统界面的弹窗修改

* 取消颜色提示和像素提示以弹窗的形式出现，改为在当前颜色下方展示
* 将颜色提示和像素提示改为随机组合的方式 :cry:

#### csv文件记录的数据修改

* 在原先的基础上，添加对颜色切换的时间和像素切换的时间 **->** 也就是将模式切换时间细分出来
* 将`模式切换错误`细分为`当前实验`的`颜色切换错误`和`像素切换错误`
* `误触发数`也一样按照模式切换错误那样来处理，为了保险起见，还是把误触发也分为`颜色误触发`和`像素误触发`
* 将时间以毫秒为单位保存

#### 传统界面的测试区域

* 在绿色测试区域加入一段缓冲区

---

### 2021-01-15

#### 传统界面的修改完成

* 把昨天对传统界面的修改完善，因为昨天已经把大部分的修改完成，但测试的时候有一个小的 **Bug** ，今天把它修改一下
* **Bug->** 当按下空格清空时，当进行第二次实验进入到颜色测试区域时，会导致颜色和像素提示一起出现，颜色提示为当前实验的目标颜色，而像素提示为上一次的像素提示
* **解决办法->** 当按下空格时，把颜色提示相关的和像素提示相关的隐藏，将`坐标`和`高度`都`设为0`
* 终于把这个界面给写完了:sob:

#### 开始着手写P-实列化界面

* 先把这个界面写出来，因为界面和传统界面一样，就是切换颜色和像素的方法不一样，这个是通过压力的值来实现颜色和像素的切换
* 这个界面最关键的还是通过对压力的实际值来对颜色和像素进行选择，这个到时候还得考虑一下
* 把这个写完后，`T-实列化`和`A-实列化`就很容易了，就是参数不一样
* 终于get了一个好的方法来实现通过压力对颜色和像素进行选择 :wink:
* 明天开始把这个功能的框架先搭出来

---

### 2021-01-16

#### 开始对P-实列化界面的核心部分进行编写

* 新建`PAExperimentPanel`类，该类主要是将笔的参数变化通过动态的方式展现出来，主要是当用户进行颜色和像素选择时，进行直观的展示，方便用户的操作和选择
* 将压力值的实时变化以动态图像的形式展现出来
* 当达到压力值条件时，弹出颜色选择和像素选择菜单
* **Bug->** 在测试压力检测中使用`repaint()`方法调用`PAExperimentPanel`类中的`paintComponent`的方法时，竟然没有反应？导致动态图像不能显示出来
* OK，对上面这个**Bug**的解决 **->** 没有把`PAExperimentPanel`加入到当前页面中，所以才没有对`repaint()`方法产生反应
* 但是正常重绘后，无法对压力值进行图像般显示 **问题根源->** 是由于在`P-实列化`中有两个区域（一个是 **画线区域**，一个是把**压力进行动态显示的区域**）所以存在监听冲突，只能对一个区域进行监听 **解决办法->** 将原来那画线区域合并到压力的动态显示区域
* 现在对`P-实列化`的界面的功能已经大致实现了，还剩下当压力达到规定值后，将颜色和像素选择菜单弹出，同时还要对颜色和像素进行选择，笔的颜色和像素也要进行相应的改变
* 但是要对可以进行颜色和像素选择的区域进行划分，不能让这个动态图像在绘制过程中一直都在

---

### 2021-01-17

#### P-实列化界面选择菜单的技术要求

* 当压力到达预设值后，颜色和像素选择菜单可以弹出，但只是显示图像，还不能进行选择操作
* 当压力到达规定值后，原来的动态显示的压力图像关闭，打开选择菜单
* 对颜色和像素菜单的编写有了一些简单的灵感，但还是要来实践来测试是否可行
* 当用户在进行选择时应该暂时取消此时笔的绘画作用，避免在选择过程中也会有笔迹产生
* 当然了，但把所有的技术都实现了，还有些东西需要微调，这个等把功能都实现后再进行微调

---

### 2021-01-18

#### 在压力到达指定值后，弹出菜单

* 菜单的位置需要是固定的 **->** 也就是说，当笔尖的压力在到达预设值时，此时应该记录下达到压力点的位置，做为菜单弹出的位置参考，同时当菜单弹出时，笔应该作为选择菜单栏的工具，而不再具备绘线功能，当选择完后，此时笔再获得绘线的功能
* 根据笔的位置来判断用户选择的是哪一个菜单栏的函数有一些问题，不能正确判断用户选择的菜单栏
* 菜单栏可以正常显示了，剩下的就是当用户抬笔后，当再次下笔时笔的压力值应该动态显示，在没有达到规定值时，选择菜单不显示，菜单的位置需要跟随鼠标的位置变化而变化
* 对用户抬笔操作后的刷新操作的逻辑有待考量 **Bug->** 当笔正常抬起时，在界面中还存在菜单选择菜单，按正常操作来说，这是不应该出现的 **解决办法->** 我感觉还是界面的刷新问题 **->** 问题原来是出在`Timer`这个类的使用方法上，每次应该是在笔尖按下时来调用它的`restart()`方法，在笔尖抬起时调用`stop()`方法
* **Bug->**在第一笔落下时，会出现一个压力的框图 **原因->** 对该框图最初定义的`boolean`值错误

---

### 2021-01-19

### 对P-实列化界面的颜色和选择菜单的优化

* 在最初的几天里，主要是实现了该界面功能的框架实现，现在是对它进行细致的优化，以达到使用的效果
* 在进行菜单栏的选择时，不用记录笔的轨迹
* 当画笔停留在颜色菜单中时，颜色分支菜单弹出，可以在分支菜单中选择想要的颜色，像素菜单也一样
* 但是触发选择分支的菜单条件还是没有想好，因为每个区域都是以坐标范围为参考的，所以这个要好好考虑一下，因为后续的颜色和像素赋值也需要通过这个判定
* 当坐标进入到颜色菜单区域时，在原来的基础上，将颜色菜单区域扩大 **->** 也就是扩大至包含颜色选择的分支菜单，这样当要进行颜色选择时，就不会出现坐标不在颜色区域时，颜色分支区域收回的情况了，像素分支菜单的处理也一样 **->** 这样的想法还是不行，还得想想其他办法 :sob:
* **解决办法：** 添加两个`boolean`类型的值，用来控制是否打开颜色和像素分支菜单，当打开颜色分支菜单时，像素分支菜单关闭，当打开像素分支菜单时，颜色分支菜单关闭
* **Bug：** 对颜色选择的结果和像素选择的结果的保存问题 **->** **原因：** 但颜色菜单被选择后，没有进行选择颜色的话就是,值就是`-1`，而在颜色分支选择菜单中，会对这个`-1`进行运算，最终导致结果为`0` **->** **解决办法：** 将颜色和像素的变化条件修改为当有颜色被选择或者像素被选择时，颜色值和像素值才会产生相应的变化，这个**Bug** 我找了好久，总算解决了 :smile:
* **Bug：** 菜单框的粗细会随着像素的选择而变化 **->** **原因：** 当笔的粗细被选择后，由于使用的是同一个`Graphics2D`，所以在将像素改变后，所有的线条的粗细都会改变 **->** **解决办法：** 当每次根据当前点的像素绘画完线条后，将线条的像素变为原样
* 明天的主要任务应该就是对在`P-实列化界面`的实验信息记录了

---

### 2021-01-20

#### P-实列化界面的完善

* 将P-实列化界面中需要记录的信息在相应位置进行设置，保证能够写入到`csv`文件中
* 设置压力调用条件的压力值需要确定
* **Bug：** 当第一次达到压力值后，第二次会很容易达到目标压力值 **原因：->** 应该是在第一次的基础上在加上了当前压力值 **解决办法：->** 在抬笔之后对`tablet`进行初始化
* `P-实列化`界面完成

---

### 2021-01-21

#### 记录信息的修改

* 对模式切换时间的记录方式修改，更贴近实际一点，待修改 **->** 修改为任务提示出现时间到切换完成结束这段时间
* 压力，方位角和倾斜角的具体记录待讨论

#### P-实例化

* 模式切换压力值的数值定义有待讨论

#### T-实例化的编写

* 开始对`T-实例化`界面进行编写，大致和`P-实例化`差不多，应该就是参数不同，一个是压力一个是倾斜角

---

### 2021-01-22

#### 将模式切换时间记录方式改变

* 新加四个变量，分别记录颜色和像素切换的开始和结束时间
* 对`PenData`类中的逻辑进行一些修改

#### T-实列化界面的编写

* 先将`T-实例化界面`大致界面写出来，因为都是实例化的界面，所以大部分都是一样的内容，就只有核心部分不一样
* 该界面的核心部分的编写还有待考虑，先看看类似的程序看看有那些可以借鉴的

---

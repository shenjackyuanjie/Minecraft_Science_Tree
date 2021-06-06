Scarpet API文档
# scarpet编程语言的基本组成部分。
 
Scarpet是一种编程语言
旨在提供编写自定义程序以在Minecraft中运行的能力
与世界互动。
 
本文档分为两部分，这一部分包括Scarpet的本身函数和Carpet控制函数，不包括任何关于Minecraft相关特性，因此可以自行运行。
 
# 概要

来看一个简单的例子吧

<pre>
script run print('Hello World!')
</pre>
 
或者一个过于复杂的例子：
 
<pre>
/script run
    block_check(x1, y1, z1, x2, y2, z2, block_to_check) ->
    (
        l(minx, maxx) = sort(l(x1, x2));
        l(miny, maxy) = sort(l(y1, y2));
        l(minz, maxz) = sort(l(z1, z2));
        'Need to compute the size of the area of course';
        'Cause this language doesn\'t support comments in the command mode';
        xsize = maxx - minx + 1;
        ysize = maxy - miny + 1;
        zsize = maxz - minz + 1;
        total_count = 0;
        loop(xsize,
            xx = minx + _ ;
            loop(ysize,
                yy = miny + _ ;
                loop(zsize,
                    zz = minz + _ ;
                    if ( block(xx,yy,zz) == block_to_check,
                        total_count += ceil(rand(1))
                    )
                )
            )
        );
        total_count
    );
    check_area_around_closest_player_for_block(block_to_check) ->
    (
        closest_player = player();
        l(posx, posy, posz) = query(closest_player, 'pos');
        total_count = block_check( posx-8,1,posz-8, posx+8,17,posz+8, block_to_check);
        print('There is '+total_count+' of '+block_to_check+' around you')
    )
/script invoke check_area_around_closest_player_for_block 'diamond_ore'
</pre>
 
或者只是一个简单的
 
<pre>
/script run print('There is '+for(rect(x,9,z,8,8,8), _ == 'diamond_ore')+' diamond ore around you')
</pre>
 
使用高级的`scarpet`函数必须提供必要的参数。

# 程序
 
你可以把程序想象成一个数学表达式,比如`"2.4*sin(45)/(2-4)"`或者`"sin(y)>0&max(z,3)>3"`。
写一个程序,就像写一个`2+3`,只是长一点。
 
## 基本语言组件
 
程序由常量组成,如`2`, `3.14`, `pi`,或`foo`,运算符,如 `+`, `/`, `->`,这些变量
可以定义,比如`foo`或将为您定义的特殊对象,比如`x`或`x`,我为每个
内置函数,以及具有名称的函数,参数形式为`f(a,b,c)`,其中`f`是函数名,
和`a,b,c`是可以是任何其他表达式的参数。这就是语言的全部,也是全部
总之-听起来很简单。
 
## 代码流
 
像任何其他合适的编程语言一样,`scarpet`需要括号,基本上用来标识东西的开始和结束
结束的地方。在使用更复杂结构的语言中,比如Java,它们倾向于使用各种类型的
它们,圆的表示函数调用,花的表示代码段,正方形表示访问列表,尖的表示
泛型类型等。。。我的意思是-没有etc,因为他们已经用尽了所有的括号选项。。。
 
`Scarpet是不同的,因为它运行的一切都基于函数(虽然它本身不是函数)
语言(如lisp)只需要圆括号,一切都由程序员来组织
它的代码是可读的,因为添加更多的括号对程序的性能没有任何影响
因为它们是在执行之前编译的。请看下面的`if()`函数用法示例：
 
<pre>
if(x&lt;y+6,set(x,8+y,z,'air');plop(x,top('surface',x,z),z,'birch'),sin(query(player(),'yaw'))&gt;0.5,plop(0,0,0,'boulder'),particle('fire',x,y,z))
</pre>
 
你喜欢看书吗
 
<pre>
if(   x&lt;y+6,
           set(x,8+y,z,'air');
           plop(x,top('surface',x,z),z,'birch'),
      sin(query(player(),'yaw'))>0.5,
           plop(0,0,0,'boulder'),
      particle('fire',x,y,z)
)
</pre>
 
或者更确切地说：
 
<pre>
if
(   x&lt;y+6,
    (
        set(x,8+y,z,'air');
        plop(x,top('surface',x,z),z,'birch')
    ),
    // else if
    sin(query(player(),'yaw'))>0.5,
    (
        plop(0,0,0,'boulder')
    ),
    // else
    particle('fire',x,y,z)
)
</pre>
 
不管你喜欢哪种款式都无所谓。这通常取决于形势和问题的复杂性
子组件。无论您添加多少空格和额外的方括号,代码的计算结果都将完全相同
相同的表达式,并且将运行完全相同的程序,所以请确保您的程序是好的和干净的,这样其他人就不会
有问题吗
 
## 功能和范围
 
用户可以以`fun(args….)->expression`的形式定义函数,并对其进行编译和保存,以备将来使用
在这个执行过程中,还可以后续调用/script命令,添加到事件等函数
分配给变量,
作为参数传递,用`call(`fun`,args…)`函数调用,但在大多数情况下
直接打电话给他们
名称,格式为`fun(args…)`。这意味着,一旦定义了函数,就可以与世界一起保存
进一步使用。对于变量,有两种类型,全局的-在代码的任何地方共享,
这些都是以`global`开头的名字,还有局部变量,其他的都是
仅在每个函数内部可见。这也意味着函数中的所有参数都是
`按值传递`,而不是`按引用传递`。
 
## 外部变量
 
通过将变量添加到函数签名包中,函数仍然可以从外部作用域`借用`变量
围绕内置函数`outer`。它将指定的值添加到函数调用堆栈中,使它们的行为完全一致
类似于在Java中捕获lambda,但与Java不同,捕获的变量不需要是final。斯卡佩特会
在函数定义时附加它们的新值,即使它们以后会更改。最有价值的是
复制但可变的值,如映射或列表,允许保留函数的`状态`,允许它们
有记忆,可以说像物体一样行动。有关详细信息,请选中`outer(var)`。
 
## 代码传递,行指示符
 
请注意,这应该只适用于粘贴要用commandblock执行的代码。Scarpet建议
应用程序中的代码(扩展名为`.sc`的文件,可以放在world files中的`/scripts`文件夹中)
或者作为一个全球可用的应用程序在singleplayer中的`.minecraft/config/carpet/scripts`文件夹中加载
作为带有命令`/script load[app name]`的Scarpet应用程序。从磁盘加载的Scarpet应用程序只应
包含代码,不需要以`/script run`前缀开头。
 
以下是可以在world`/scripts`文件夹中的`foo.sc` app文件中提供的代码
 
<pre>
run_program() -> (
  loop( 10,
    // looping 10 times
    // comments are allowed in scripts located in world files
    // since we can tell where that line ends
    foo = floor(rand(10));
    check_not_zero(foo);
    print(_+' - foo: '+foo);
    print('  reciprocal: '+  _/foo )
  )
);
check_not_zero(foo) -> (
  if (foo==0, foo = 1)
)
</pre>
 
我们称之为游戏中的：
 
<pre>
/script load foo
/script in foo invoke run_program
</pre>
 
但是,以下代码也可以作为命令或命令块输入。
 
由于可以输入聊天室的最大命令长度有限,因此您可能需要插入
通过将它们粘贴到命令块或从世界文件中读取来执行程序,但是粘贴到命令块将
删除一些空白并压扁新行,使代码不可读。如果您粘贴的程序
是完美的,永远不会造成错误,我向你致敬,但在大多数情况下,它很可能是你的程序
可能会在编译时(最初分析时)或执行时(突然尝试
把某物除以零。在这些情况下,您可能希望得到一条有意义的错误消息,但为此,您需要
需要为编译器指出这些新行放在哪里,因为命令块会压扁它们。为此,
放在行的开头,让编译器知道您在哪里。这使得`$`是唯一的
在程序中是非法的字符,因为它将被替换为新行。据我所知,`$`不是
在Minecraft标识符中的任何地方使用,所以这不应该妨碍你程序的能力。
 
考虑以下作为命令块命令执行的程序：
 
<pre>
/script run
run_program() -> (
loop(10,
foo=地板(rand());
check_not_zero(foo);
print(`+`-foo:`+foo`);
print(`交互：`+0/foo)
)
);
check_not_zero(foo)->(
if (foo==0,foo=1)
)
</pre>
 
假设其目的是检查条是否为零,并防止在打印时被零除,但因为
`foo`作为变量传递,它从不更改原始foo值。因为不可避免的分裂
到0,我们得到以下信息：
 
<pre>
Your math is wrong, Incorrect number format for NaN at pos 98
run_program() -> ( loop( 10, foo = floor(rand(_)); check_not_zero(foo); print(_+' - foo: '+foo);
HERE>> print(' reciprocal: '+ _/foo ) ));check_not_zero(foo) -> ( if (foo==0, foo = 1))
</pre>
 
如我们所见,我们得到的问题,数学运算的结果不是一个数字
(无穷大,所以不是一个数字),但是通过将我们的程序粘贴到命令中,使它如此压缩换行符
虽然很清楚错误发生在哪里,我们仍然可以跟踪错误,但是错误的位置(98)
是不是很有帮助,也不会有用,如果该程序得到显着更长。为了解决这个问题,我们可以
在脚本的每一行前面加上美元符号`$`：
 
<pre>
/script run
$run_program() -> (
$  loop( 10,
$    foo = floor(rand(_));
$    check_not_zero(foo);
$    print(_+' - foo: '+foo);
$    print('  reciprocal: '+  _/foo )
$  )
$);
$check_not_zero(foo) -> (
$   if (foo==0, foo = 1)
$)
</pre>
 
然后我们得到以下错误消息
 
<pre>
Your math is wrong, Incorrect number format for NaN at line 7, pos 2
  print(_+' - foo: '+foo);
   HERE>> print(' reciprocal: '+ _/foo )
  )
</pre>
 
正如我们所注意到的,我们不仅得到了更简洁的片段,还得到了关于行号和位置的信息,
所以这意味着它更容易找到潜在的问题
 
显然这不是我们计划的工作方式。要通过函数调用修改foo,
我们要么返回结果并将其赋给新变量：
 
<pre>
foo = check_not_zero(foo);
...
check_not_zero(foo) -> if(foo == 0, 1, foo)
</pre>
 
.. 或者将其转换为全局变量,在本例中,不需要将其作为参数传递
 
<pre>
global_foo = floor(rand(10));
check_foo_not_zero();
...
check_foo_not_zero() -> if(global_foo == 0, global_foo = 1)
</pre>
 
## Scarpet预处理器
 
有几个预处理操作应用于程序的源代码,以清理它并准备
执行。其中一些会影响您的代码,因为它是通过堆栈跟踪和函数定义报告的,还有一些
仅适用于表面。
-剥离`//`注释(在文件模式下)
-用换行符替换`$`(在命令模式下,修改提交的代码)
-删除不跟在`;`后面的分号用作二进制运算符,允许轻松使用分号
-将`{`翻译成`m(`,`[`翻译成`l(`,`]`和`}`翻译成`)`
 
当前没有对代码应用进一步的优化。
 
## 提到
 
LR1解析器,标记器和几个内置函数都是基于EvalEx项目构建的。
EvalEx是一个方便的Java表达式计算器,它允许
简单的数学和布尔表达式。EvalEx是在麻省理工学院许可下发行的。
有关更多信息,请参阅：[EvalEx GitHub repository](https://github.com/uklimaschewski/EvalEx)
#变量和常量
 
`scarpet `提供了许多可以在脚本中直接使用的常量
 
*`null`：没什么,zilch,甚至不假
*`true`：纯true,可以充当`1`
*`false`：假真或真假等于 `0` `
*·pi·：对于周长的扇子来说,它是直径为1的苹果pi的周长。约3.14
*欧拉：聪明的家伙。其指数的导数为1\。约2.72
 
除此之外,还有一堆系统变量,以``开头,由``scarpet`内置设置,
像```,通常是循环中的每个连续值,``i`表示迭代,或者``a`像累加器
用于`reduce`函数。对Minecraft特定调用的某些调用也会设置` x`,` y`,` z`,表示
封锁阵地。所有以``开头的变量都是只读的,不能在客户机代码中声明和修改。
 
## 文字
 
`scarpet`接受数字和字符串常量。数字看起来像`1,2.5,-3e-7,0xff,`并且是内部的
主要表示为Java的`double`,但是`scarpet`会尽可能地修剪尾随的零,所以如果
需要将它们用作整数甚至长-你可以。长值也不会失去它们的长精度,
减法,求反和乘法,但是不能保证返回长值的任何其他运算
(如除法)在一个数字上,即使它可以适当地
表示为long,将使它们转换为double。
 
字符串使用单引号有多种原因,但主要是为了允许
在双引号命令参数中更容易使用字符串(将脚本作为`/script fill`的参数传递时)
例如),或者在scarpet中输入json(例如反馈到`/data merge`命令中)。
在普通字符串和正则表达式中,字符串还使用反斜杠`\`来引用特殊字符
 
<pre>
'foo'
print('This doesn\'t work')
nbt ~ '\\.foo'   // matching '.' as a '.', not 'any character match'
</pre>
# 操作员
 
在表达式中可以使用许多运算符。这些可以被视为泛型类型运算符
适用于大多数数据类型。它们还遵循标准运算符优先级,即理解`2+2*2`
作为`2+(2*2)`,而不是`(2+2)*2`,否则从左到右应用,即解释`2+4-3`
作为`(2+4)-3`,这在数字的情况下并不重要,但是因为``scarpet`允许混合所有值类型
关联性很重要,可能会导致意外的影响：
 
重要的运算符是函数定义`->`运算符。它将被覆盖
在[用户定义函数和程序控制流](docs/scarpet/language/Functions和controlflow.md)中
 
<pre>
'123'+4-2 => ('123'+4)-2 => '1234'-2 => '134'
'123'+(4-2) => '123'+2 => '1232'
3*'foo' => 'foofoofoo'
1357-5 => 1352
1357-'5' => 137
3*'foo'-'o' => 'fff'
[1,3,5]+7 => [8,10,12]
</pre>
 
如您所见,在同一表达式中与其他类型混合时,值的行为可能不同。
如果值的类型相同,结果往往很明显,但`Scarpet`试图理解任何东西
它必须处理
 
## 运算符优先级
 
下面是`scarpet`中操作符的完整列表,包括控制流操作符。注意,逗号和括号
从技术上讲,它们不是运算符,而是语言的一部分,即使它们看起来像：
 
*匹配,获取`~：`
*一元`+-`
*指数`^`
*乘法`*/%`
*加法`+-`
*比较`>>=<=<`
*平等`===`
*逻辑与`&&`
*逻辑或`||`
*作业`=+=<>`
*定义`->`
*下一个语句``
*逗号``
*括号`()`
 
### `Get,访问器运算符：`
 
`get(…)`函数的运算符版本,用于访问列表,映射和其他容器的元素
(即NBTs)。区分`~`运算符很重要,它是一个匹配运算符,期望
执行一些额外的计算来检索结果,而`:`应该是直接的,即时的,并且
源对象的行为应类似于容器,并支持完整容器API,
表示`get(…)``,`put(…)``,`delete(…)``和`has(…)`函数`
 
对于某些运算符和函数(get,put,delete,has,=,+=),对象可以使用`:`注释字段作为l值,
意构式`foo:0 = 5`,类似于`put(foo,0,5)`,而不是`get(foo,0)=5`,
这会导致一个错误。
 
TODO:添加有关l值行为的更多信息。
 
### `匹配运算符~`
 
此运算符应理解为`matches`,`contains`,`is in`或`find me something about something`。
对于字符串,它将右操作数作为正则表达式匹配到左操作数,返回：
-如果不匹配,则返回`null`
-匹配短语(如果未应用分组)
-匹配元素(如果应用了一个组)
-如果应用了多个分组,则列出匹配项
 
这可用于以更复杂的方式从未分析的nbt中提取信息(使用`get(…)`for
更合适的方法)。对于列表,它检查元素是否在列表中,并返回
该元素的索引,如果找不到此类元素,则为`null`,特别是使用`first(…)`
函数不会返回索引。目前它对数字没有任何特殊的行为-它检查
左操作数相对于上正则表达式的字符串表示中的字符的存在性
右手边。
 
在Minecraft API中,`entity~feature`部分是`query(entity,feature)`的快捷码,用于不接受
任何额外的参数。
 
<pre>
[1,2,3] ~ 2  => 1
[1,2,3] ~ 4  => null

'foobar' ~ 'baz'  => null
'foobar' ~ '.b'  => 'ob'
'foobar' ~ '(.)b'  => 'o'
'foobar' ~ '((.)b)'  => ['ob', 'o']
'foobar' ~ '((.)(b))'  => ['ob', 'o', 'b']
'foobar' ~ '(?:(.)(?:b))'  => 'o'

player('*') ~ 'gnembon'  // null unless player gnembon is logged in (better to use player('gnembon') instead
p ~ 'sneaking' // if p is an entity returns whether p is sneaking
</pre>
 
或者是一个寻找鱿鱼的无效方法的较长的例子
 
<pre>
entities = entities_area('all',x,y,z,100,10,100);
sid = entities ~ 'Squid';
if(sid != null, run('execute as '+query(get(entities,sid),'id')+' run say I am here '+query(get(entities,sid),'pos') ) )
</pre>
 
或者一个例子,以找出如果一个球员有一个特定的附魔持斧(任何一只手),并获得其水平
(未通过`get(…)``使用正确的NBTs查询支持)：
 
<pre>
global_get_enchantment(p, ench) -> (
$   for(['main','offhand'],
$      holds = query(p, 'holds', _);
$      if( holds,
$         [what, count, nbt] = holds;
$         if( what ~ '_axe' && nbt ~ ench,
$            lvl = max(lvl, number(nbt ~ '(?<=lvl:)\\d') )
$         )
$      )
$   );
$   lvl
$);
/script run global_get_enchantment(player(), 'sharpness')
</pre>
 
### `基本算术运算符+-*/`
 
允许添加两个表达式的结果。如果操作数解析为数字,则结果是算术运算。
对于字符串,从字符串中加或减将导致字符串串联和删除子字符串
从那根绳子上。字符串和数字相乘的结果是将字符串重复N次并得到除法结果
在字符串的第一个第k部分,所以`str*n/n~str`
 
如果第一个操作数是一个列表,则
结果是一个新的列表,其中所有元素都被另一个操作数逐个修改,或者如果该操作数是一个列表
具有相同的项数-按元素进行加法/减法。这将优先考虑将列表视为值容器
作为向量处理的列表。
 
使用maps(`{}`或`m()`)进行加法,如果两个操作数都是maps,则会产生一个新的maps,其中两个maps的键都被添加,
将right参数的元素添加到键,left map的元素,或者只是将right值添加为新键
在输出映射中。
 
示例：
 
<pre>
2+3 => 5
'foo'+3+2 => 'foo32'
'foo'+(3+2) => 'foo5'
3+2+'bar' => '5bar'
'foo'*3 => 'foofoofoo'
'foofoofoo' / 3 => 'foo'
'foofoofoo'-'o' => 'fff'
[1,2,3]+1  => [2,3,4]
b = [100,63,100]; b+[10,0,10]  => [110,63,110]
{'a' -> 1} + {'b' -> 2} => {'a' -> 1, 'b' -> 2}
</pre>
 
### `仅运算符%^`
 
只有当两个操作数都是数字时,模和指数(幂)运算符才起作用
 
<pre>
pi^pi%euler  => 1.124....
-9 % 4  => -1
9 % -4  => 0 ¯\_(ツ)_/¯ Java
-3 ^ 2  => 9
-3 ^ pi => // Error
</pre>
 
### `比较运算符==！=<><=>=`
 
允许比较两个表达式的结果。对于数字,它考虑的是数字的算术顺序
字符串-按字典顺序排列,空值总是比其他值`少`,列表检查它们的元素-
如果大小不同,则大小很重要,否则,将对每个元素执行成对比较。
与所有这些运算符相同的顺序规则用于`sort`使用的默认排序顺序
功能。所有这些都是正确的：
 
<pre>
null == null
null != false
0 == false
1 == true
null < 0
null < -1000
1000 < 'a'
'bar' < 'foo'
3 == 3.0
</pre>
 
### `逻辑运算符&&||`
 
这些运算符分别计算操作数上的布尔运算。重要的是如果计算
第二个操作数不是必需的,它不会被计算,这意味着可以将它们用作条件语句。在
case of success返回第一个正操作数(` | `)或最后一个正操作数(`&&`)。
 
<pre>
true || false  => true
null || false => false
false || null => null
null != false || run('kill gnembon')  // gnembon survives
null != false && run('kill gnembon')  // when cheats not allowed
null != false && run('kill gnembon')  // gnembon dies, cheats allowed
</pre>
 
### `赋值运算符=<>+=`
 
一组赋值运算符。在LHS上都需要有界变量,`<>`在LHS上需要有界参数
右边也是(有界的,意思是变量)。此外,它们还可以处理列表构造函数
使用所有有界变量,然后作为列表赋值运算符。当在列表上使用`+=`时,它会扩展
元素的列表,并返回列表(old==new)`scarpet`当前不支持删除项目。
可以通过`filter`命令删除项目,并将其重新分配给同一变量。两次行动都会
无论如何都需要重写数组。
 
<pre>
a = 5  => a == 5
[a,b,c] = [3,4,5] => a==3, b==4, c==5
[minx,maxx] = sort(xi,xj);  // minx assumes min(xi, xj) and maxx, max(xi, xj)
[a,b,c,d,e,f] = [range(6)]; [a,b,c] <> [d,e,f]; [a,b,c,d,e,f]  => [3,4,5,0,1,2]
a = [1,2,3]; a += 4  => [1,2,3,4]
a = [1,2,3,4]; a = filter(a,_!=2)  => [1,3,4]
</pre>
 
### `一元运算符-+`
 
需要一个数字,翻转标志。断言它是一个数字的一种方法是使脚本崩溃。gg。
 
<pre>
-4  => -4
+4  => 4
+'4'  // Error message
</pre>
 
### `求反运算符 !`
 
翻转表达式的布尔条件。等价于`bool(expr)==false`
 
<pre>
!true  => false
!false  => true
!null  => true
!5  => false
!l() => true
!l(null) => false
</pre>
 
### `解包运算符 ...`
 
将迭代器列表中的元素解压到函数中的一系列参数中,从而生成`fun(…[1,2,3])`
与`fun(1,2,3)`相同。对于映射,它将它们解压到一个键值对列表中。
 
在函数签名中,它标识vararg参数。
 
<pre>
fun(a, b, ... rest) -> [a, b, rest]; fun(1, 2, 3, 4)    => [1, 2, [3, 4]]
</pre>
 
`…`的效果可以惊人地持久。它通过使用变量和函数调用来保持。
 
<pre>
fun(a, b, ... rest) -> [a, b, ... rest]; fun(1, 2, 3, 4)    => [1, 2, 3, 4]
args() -> ... [1, 2, 3]; sum(a, b, c) -> a+b+c; sum(args())   => 6
a = ... [1, 2, 3]; sum(a, b, c) -> a+b+c; sum(a)   => 6
</pre>
 
解包机制可以用于列表和映射压缩,而不仅仅是函数调用。
 
<pre>
[...range(5), pi, ...range(5,-1,-1)]   => [0, 1, 2, 3, 4, 3.14159265359, 5, 4, 3, 2, 1, 0]
{ ... map(range(5),  _  -> _*_ )}   => {0: 0, 1: 1, 2: 4, 3: 9, 4: 16}
{...{1 -> 2, 3 -> 4}, ...{5 -> 6, 7 -> 8}}   => {1: 2, 3: 4, 5: 6, 7: 8}
</pre>
 
精细打印：参数列表的解包发生在函数求值之前。
这意味着在某些情况下,例如
当需要表达式(`map(list,expr)`)或函数不应计算某些(most！)它的参数(`if(…)`),
解包无法使用,将被忽略,留下`。。。list`与`list`相同。
不尊重解包机制的函数,一开始就不应该使用它
(即,有一个或非常明确的,非常具体的参数),
因此,建议谨慎(测试前)。其中一些多参数内置函数
`if`,`try`,`sort key`,`system variable get`,`synchronize`,`sleep`,`in dimension`,
所有容器函数(`get`,`has`,`put`,`delete`),
以及所有循环函数(`while`,`loop`,`map`,`filter`,`first`,`all`,`c_for`,`for`和`reduce`)。
 
# 算术运算
 
## 基本算术函数
 
他们有很多人——他们需要一个数字,然后吐出一个数字,做你希望他们做的事情。
 
### `fact(n)`
 
一个数的阶乘,又称n,只是不在`scarpet`里。变大了。。。快。。。
 
### `sqrt(n)`
 
平方根。对于其他花哨的词根,请使用`^`,数学和yo noggin。想象一下树上的平方根。。。
 
### `abs(n)`
 
绝对值。
 
### `round(n)`
 
最接近的整数值。你知道地球也是圆的吗?
 
### `floor(n)`
 
仍然不大于`n`的最大整数。在这里插入地板双关语。
 
### `ceil(n)`
 
第一个不小于`n`的幸运整数。正如你所料,天花板通常就在地板的正上方。
 
### `ln(n)`
 
n的自然对数。当然。
 
### `ln1p(n)`
 
n+1的自然对数。非常乐观。
 
### `log10(n)`
 
n的十进制对数。它的天花板和地板一样长。
 
### `log(n)`
 
n的二元对数。最后,一个合适的,不像前11个。
 
### `log1p(n)`
 
n+1的二元对数。也总是积极的。
 
### `mandelbrot(a,b,极限)`
 
为集合`a`和`b`计算mandelbrot集合的值。找出甲虫。为什么不。
 
### `min(arg,…),min(列表),max(arg,…),max(列表)`
 
假设默认排序顺序,计算提供的参数的最小值或最大值。
如果缺少argmax,只需使用a~max(a),效率稍低,但仍然很有趣。
 
有趣的位-`min`和`max`不会从参数中删除变量关联,这意味着可以用作
赋值的LHS(明显的情况),或者函数定义中的参数spec(远不那么明显)。
 
<pre>
a = 1; b = 2; min(a,b) = 3; l(a,b)  => [3, 2]
a = 1; b = 2; fun(x, min(a,b)) -> l(a,b); fun(3,5)  => [5, 0]
</pre>
 
完全不知道,后者在实践中如何有用。但既然它编译好了,就可以发货了。
 
### `relu(n)`
 
n的线性整流器。0低于0,n高于0。为什么不呢`max(0,n)`道德影响较小。
 
## 三角函数/几何函数
 
### `sin(x)`

### `cos(x)`

### `tan(x)`

### `asin(x)`

### `acos(x)`

### `atan(x)`

### `atan2(x,y)`

### `sinh(x)`

### `cosh(x)`

### `tanh(x)`

### `sec(x)`

### `csc(x)`

### `sech(x)`

### `csch(x)`

### `cot(x)`

### `acot(x)`

### `coth(x)`

### `asinh(x)`

### `acosh(x)`

### `atanh(x)`

### `rad(deg)`

### `deg(rad)`
 
随便用，请
# 系统功能
 
## 类型转换函数
 
### `copy(expr)`
 
返回表达式的深度副本。可用于复制可变对象,如地图和列表
 
### `type(expr)`
 
返回指示表达式类型的字符串值。可能的结果
是`null`,`number`,`string`,`list`,`map`,`iterator`,`function`,`task`,
以及`block`,`entity`,`nbt`,`text`等与地雷相关的概念。
 
### `bool(expr)`
 
返回表达式的布尔上下文。
Bool还将字符串值解释为boolean值,这与其他方法不同
可以使用布尔上下文的位置。这可用于API函数向其返回字符串值的地方
表示二进制值。
 
bool(pi) => true
bool(false) => false
bool('') => false
bool(l()) => false
bool(l('')) => true
bool('foo') => true
bool('false') => false
bool('nulL') => false
if('false',1,0) => true
</pre>
 
### `number(expr)`
 
返回表达式的数字上下文。可用于从字符串或其他类型读取数字
 
<pre>
number(null) => 0
number(false) => 0
number(true) => 1
number('') => null
number('3.14') => 3.14
number(l()) => 0
number(l('')) => 1
number('foo') => null
number('3bar') => null
number('2')+number('2') => 4
</pre>
 
### `str(expr)`,`str(expr, params? ... )`, `str(expr, param_list)`
 
如果用一个参数调用,则返回该值的字符串表示形式。
 
否则,返回表示表达式的格式化字符串。格式化的参数可以提供为
每个连续的参数,或作为一个列表,然后将是唯一的额外参数。格式化一个列表参数
,可以使用`str(list)`或`str(`foo%s`,l(list))`。
 
接受`String.format`接受的格式样式。
支持的类型(使用``%<?>``语法)：
 
*   `d`, `o`, `x`: integers, octal, hex
*   `a`, `e`, `f`, `g`: floats
*   `b`: booleans
*   `s`: strings
*   `%%`: '%' character
 
<pre>
str(null) => 'null'
str(false) => 'false'
str('') => ''
str('3.14') => '3.14'
str([]) => '[]'
str(['']) => '[]'
str('foo') => 'foo'
str('3bar') => '3bar'
str(2)+str(2) => '22'
str('pi: %.2f',pi) => 'pi: 3.14'
str('player at: %d %d %d',pos(player())) => 'player at: 567, -2423, 124'
</pre>
 
* * *
## 线程和并行执行
 
Scarpet允许与主脚本执行线程并行运行执行线程。在Minecraft中,应用程序
在主服务器线程上执行。既然雷艇本身就不是线程安全的,那就不是了
有利于并行执行,以便更快地访问世界资源。`getBlockState`和`setBlockState`
不是线程安全的,需要将执行停在服务器线程上,这些请求可以在服务器线程中执行
两次滴答之间的停止滴答的时间,并不需要全部50毫秒。但是并行运行也有好处,
像精细的时间控制不依赖滴答的时钟,或运行的东西相互独立。你还可以跑
您的操作是逐点进行的,或者使用`game tick()`API函数控制执行
(讨厌的解决方案),或者使用 `schedule()` 函数调度tick(首选解决方案),但是线程提供了更多的控制
在时间上不影响主博弈,是解决并行问题的唯一方法
(参见[scarpet camera](/src/main/resources/assets/carpet/scripts/camera.sc))。
 
由于游戏的限制,线程也有一些限制。你不能因为
实例 `join_task()` 完全来自主脚本和服务器线程,因为任何对Minecraft的使用都是特定的
需要任何世界访问的函数,将需要在主线程上驻车并连接以获得世界访问,
这意味着对该任务调用join将不可避免地导致典型的死锁。你仍然可以加入任务
因为在这种情况下,死锁的唯一可能来自
糟糕的代码,而不是内部世界访问行为。有些事情很棘手,比如玩家或实体操纵,可能是
有效地并行化。
 
如果应用程序正在关闭,则通过`task`创建新任务将不会成功。相反,新任务将什么也不做而返回
`null`,因此大多数线程化应用程序应该能够自然地处理关闭的应用程序。请记住,如果您依赖于任务返回值,
不管在这些情况下发生什么,它们都将返回`null`。当应用程序处理` on close()`事件时,新任务将无法执行
此时无法提交,但当前任务不会终止。应用程序可以利用这个机会优雅地关闭它们的任务。
无论应用程序是否处理` on close()`事件,或对其中的任务执行任何操作,所有任务都将异常终止
在接下来的1.5秒内。
 
### `task(function, ... args)`, `task_thread(executor, function, ... args)`
 
创建并运行并行任务,将句柄返回给任务对象。任务将返回
函数,或者在任务仍在进行时立即返回`null`,因此获取
任务对象是非阻塞的。函数可以是函数值,函数lambda或现有函数的名称
定义的函数。如果函数需要参数来调用,则应在函数之后提供参数
名称或值``task thread`中的executor`identifier`将任务放入由该值标识的特定队列中。
默认线程值是`null`线程。对于任何执行者,并行任务的数量没有限制,
因此,使用不同的队列仅用于同步目的。
 
<pre>
task( _() -> print('Hello Other World') )  => Runs print command on a separate thread
foo(a, b) -> print(a+b); task('foo',2,2)  => Uses existing function definition to start a task
task_thread('temp', 'foo',3,5);  => runs function foo with a different thread executor, identified as 'temp'
a = 3; task_thread('temp', _(outer(a), b) -> foo(a,b), 5)  
    => Another example of running the same thing passing arguments using closure over anonymous function as well as passing a parameter.
</pre>
 
如果您想基于模块中未定义的函数创建任务,请阅读上的提示
`将函数引用传递给应用程序的其他模块`部分。
 
### `sleep()` `sleep(timeout)`, `sleep(timeout, close_expr)`
 
 
暂停线程(或游戏本身,如果不是作为任务的一部分运行)的执行 `expr` 毫秒。
它检查中断的执行,在这种情况下退出线程(或者整个程序,如果不是在线程上运行的话)以防应用程序
正在停止/删除。如果指定了关闭表达式,则在触发关闭信号时执行该表达式。
如果在主线程上运行(即不作为任务运行),则只有在整个游戏关闭时才能调用close表达式,因此只能调用close
对线程有意义。对于常规程序,请使用` on close()`处理程序。
 
由于`close_expr`是在应用程序关闭启动后执行的,因此您将无法在该块中创建新任务。线程
应该定期调用`sleep`,以确保所有应用程序任务在应用程序关闭时或之后立即完成,但应用程序引擎
不会强制删除正在运行的任务,因此任务本身需要正确响应关闭请求。
 
<pre>
sleep(50)  # wait for 50 milliseconds
sleep(1000, print('Interrupted')) # waits for 1 second, outputs a message when thread is shut down.
</pre>
 
### `task_count(executor?)`
 
如果未提供参数,则返回此时使用scarpet并行执行的任务总数
穿线系统。如果提供了执行器,则返回该提供程序的活动任务数。使用 `task_count(null)` 
仅获取默认执行器的任务计数。
 
### `task_value(task)`
 
返回任务返回值,如果任务尚未完成,则返回 `null`。这是一个非阻塞操作。与 `join task` 不同,
可以在任何时候调用任何任务
 
### `task_join(task)`
 
等待任务完成并返回其计算值。如果任务已经完成,则立即返回它。
除非直接获取任务值,即通过 `task value` ,否则此操作是阻塞的。因为Minecraft有一个
限制所有世界访问操作必须在非计时时间在主游戏线程上执行,
从主线程加入任何使用Minecraft API的任务意味着自动锁定,因此从主线程加入
不允许线程。如果确实需要,可以从其他线程加入任务,或者与其他线程异步通信
任务通过全局或函数数据/参数来监视其进度,通信,获得部分结果,
或信号终止。
 
### `task_completed(task)`
 
如果任务已完成,则返回true,否则返回false。
 
### `synchronize(lock, expression)`
 
计算与锁 `lock` 同步的 `expression` 。返回表达式的值。
 
### `task_dock(expr)`
 
在not任务(在游戏主线程上运行常规代码)中,它是一个传递命令。在任务中-it对接
主服务器线程上的当前线程,并将表达式作为一个服务器脱机服务器任务执行。
如果一个任务有几个停靠操作要执行,例如设置块和
一次完成所有这些任务比在每个调用中打包每个块访问要高效得多。
 
请注意,停靠任务意味着tick的执行将被延迟,直到表达式被计算。
这将使用 `task dock` 将您的任务与其他任务同步,但如果您应该使用 `synchronize` 来
同步任务而不锁定主线程。
 
 
* * *
 
## 辅助功能
 
### `lower(expr), upper(expr), title(expr)`
 
返回所传递表达式的字符串表示形式的小写,大写或titlecase表示形式
 
<pre>
lower('aBc') => 'abc'
upper('aBc') => 'ABC'
title('aBc') => 'Abc'
</pre>
 
### `replace(string, regex, repl?); replace_first(string, regex, repl?)`
 
用 `repl` 表达式替换字符串中所有正则表达式或第一次出现的正则表达式,
或者什么都没有,如果没有指定的话
 
<pre>
replace('abbccddebfg','b+','z')  // => azccddezfg
replace_first('abbccddebfg','b+','z')  // => azccddebfg
</pre>
 
### `length(expr)`
 
返回表达式的长度,字符串的长度,数字整数部分的长度,
或者名单的长度
 
<pre>
length(pi) => 1
length(pi*pi) => 1
length(pi^pi) => 2
length(l()) => 0
length(l(1,2,3)) => 3
length('') => 0
length('foo') => 3
</pre>
 
### `rand(expr), rand(expr, seed)`
 
返回从 `0.0` (包含)到 `expr` (排除)的随机数。在布尔上下文中(在条件中,
如果随机选择的值小于1,则布尔函数(或`bool`)返回false。这意味着
`bool(rand(2))`在一半时间内返回真值,`!rand(5)`返回20%(1/5)时间的真值。如果种子不是
提供,使用一个随机种子,在所有的scarpet应用程序中共享。
如果提供了seed,则对rand()的每个连续调用都将像对
同一个随机物体。Scarpet跟踪多达65536个自定义随机数生成器(每个应用程序都有自定义种子),
所以如果你超过这个数字,你的随机序列会回到开始,重新开始。
 
<pre>
map(range(10), floor(rand(10))) => [5, 8, 0, 6, 9, 3, 9, 9, 1, 8]
map(range(10), bool(rand(2))) => [false, false, true, false, false, false, true, false, true, false]
map(range(10), str('%.1f',rand(_))) => [0.0, 0.4, 0.6, 1.9, 2.8, 3.8, 5.3, 2.2, 1.6, 5.6]
</pre>
 
## `reset_seed(seed)`
 
将`rand`为此种子使用的随机化器序列重置为其初始状态。返回布尔值
指示给定的种子是否已被使用。
 
### `perlin(x), perlin(x, y), perlin(x, y, z), perlin(x, y, z, seed)`
 
对于1,2或3维坐标,返回从 `0.0` 到 `1.0` (大致)的噪波值。它采样的默认种子
from是 `0` ,但seed也可以指定为第4个参数。如果需要自定义的1D或2D噪波值
seed,对 `z` 使用 `null` ,或分别使用 `y` 和 `z` 参数。
 
Perlin噪波基于正方形网格,生成的贴图比单纯形更粗糙,单纯形更粗糙。
查询较低维度的结果,而不是将未使用的维度附加到常量中,有助于提高速度,
 
你不应该从经常变化的种子中取样。Scarpet将跟踪最后256颗柏林种子
用于提供与默认种子 `0` 类似的速度的采样。以防应用程序引擎使用更多
比同时使用256个种子要昂贵得多。
 
### `simplex(x, y), simplex(x, y, z), simplex(x, y, z, seed)`
 
对于二维或三维坐标,返回从 `0.0` 到 `1.0` (大致)的噪波值。它采样的默认种子
from是 `0` ,但seed也可以指定为第4个参数。如果需要自定义种子的2D噪波值,
对`z`参数使用`null`。
 
单纯形噪声是基于一个三角形网格和生成平滑的地图相比,柏林。取样1D单纯形
噪声,将其他坐标附加到常数。
 
你不应该从经常变化的种子中取样。Scarpet将跟踪最后256个单纯形种子
用于提供与默认种子 `0` 类似的速度的采样。以防应用程序引擎使用更多
比同时使用256个种子要昂贵得多。
 
### `print(expr)`, `print(player, expr)`
 
打印要聊天的表达式的值。将参数的结果原封不动地传递给输出,
所以 `print` 语句可以编入代码来调试编程问题。默认情况下,它使用相同的通信
大多数普通命令使用的通道。
 
如果直接指定了player,它只向该player发送消息,如 `tell` 命令。
 
<pre>
print('foo') => results in foo, prints: foo
a = 1; print(a = 5) => results in 5, prints: 5
a = 1; print(a) = 5 => results in 5, prints: 1
print('pi = '+pi) => prints: pi = 3.141592653589793
print(str('pi = %.2f',pi)) => prints: pi = 3.14
</pre>
 
### `time()`
 
返回自`some point`以来的毫秒数,如Java的`System.nanoTime()`,该值因系统而异
从Java到Java。此度量值不应用于确定当前(日期)时间,而应用于度量
事物的持续时间。
为了方便起见,它返回一个以毫秒(ms)为单位的浮点值(μs) 理智的决心。
 
 
<pre>
start_time = time();
flip_my_world_upside_down();
print(str('this took %d milliseconds',time()-start_time))
</pre>
 
### `unix_time()`
 
返回标准POSIX时间(以毫秒为单位,从epoch开始)
(1970年1月1日凌晨00点0秒)。
与前面的函数不同,这可以用来获得准确的时间,但它因时区而异。
 
### `convert_date(milliseconds)`
### `convert_date(year, month, date, hours?, mins?, secs?)`
### `convert_date(l(year, month, date, hours?, mins?, secs?))`
 
如果使用单个参数调用,则将标准POSIX时间转换为以下格式的列表：
 
`l(year, month, date, hours, mins, secs, day_of_week, day_of_year, week_of_year)`
 
例如：`convert_date(1592401346960) -> [2020, 6, 17, 10, 42, 26, 3, 169, 25]`
 
其中`6`代表六月,`17`代表十七日,`10`代表上午十点,
`42代表一小时过去42分钟,26代表一分钟过去26秒,
`3`代表星期三,`169`是一年中的一天,`25`是一年中的一周。
 
运行`convert date(unix time())`以列表形式获取当前时间。
 
 
使用列表或3或6个参数调用时,返回标准POSIX时间(以毫秒为单位,从
新纪元的开始(1970年1月1日),
使用输入到函数中的时间,而不是系统时间。
 
编辑示例：
<pre>
date = convert_date(unix_time());

months = l('Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec');

days = l('Mon','Tue','Wed','Thu','Fri','Sat','Sun');

print(
  str('Its %s, %d %s %d, %02d:%02d:%02d', 
    days:(date:6-1), date:2, months:(date:1-1), date:0, date:3, date:4, date:5 
  )
)  
</pre>
 
这会给你一个日期：
 
It is currently `hrs`:`mins` and `secs` seconds on the `date`th of `month`, `year`
 
### `encode_b64(string)`, `decode_b64(string)`
 
对b64中的字符串进行编码或解码,如果无效,则引发`b64 error`异常
 
### `encode json(value)`,`decode json(string)`
 
将值编码为json字符串,并将json字符串解码为有效值,如果出现`json error`异常,则引发异常
无法正确分析
 
### `profile expr(expr)`
 
返回给定表达式在50ms时间内可以运行的次数。有助于分析和优化代码。
注意,即使它只是一个数字,它也会运行这些命令,所以如果它们是破坏性的,您需要小心。
 
* * *
 
## 访问变量和存储函数(谨慎使用)
 
### `var(expr)`
 
返回表达式的字符串值名称下的变量。允许以更多方式操作变量
编程方式,允许使用具有哈希映射类型键值访问的局部变量集,
也可以与全局变量一起使用
 
<pre>
a = 1; var('a') = 'foo'; a => a == 'foo'
</pre>
 
### `undef(expr)`
 
删除名为`expr`的变量的所有绑定。同时删除所有具有该名称的函数定义。
它可以影响全局变量池和特定函数的局部变量集。
 
<pre>
inc(i) -> i+1; foo = 5; inc(foo) => 6
inc(i) -> i+1; foo = 5; undef('foo'); inc(foo) => 1
inc(i) -> i+1; foo = 5; undef('inc'); undef('foo'); inc(foo) => Error: Function inc is not defined yet at pos 53
undef('pi')  => bad idea - removes hidden variable holding the pi value
undef('true')  => even worse idea, unbinds global true value, all references to true would now refer to the default 0
</pre>
 
### `vars(prefix)`
 
它返回本地范围(如果前缀不是以`global`开头)或全局变量的所有变量名
(否则)。下面是一个较大的示例,它使用`vars`和`var`函数的组合来
用于物品计数
 
<pre>
/script run
$ count_blocks(ent) -> (
$   l(cx, cy, cz) = query(ent, 'pos');
$   scan(cx, cy, cz, 16, 16, 16, var('count_'+_) += 1);
$   for ( sort_key( vars('count_'), -var(_)),
$     print(str( '%s: %d', slice(_,6), var(_) ))
$   )
$ )
/script run count_blocks(player())
</pre>
 
* * *
 
## 系统键值存储
 
Scarpet独立运行应用程序。可以通过使用共享库来共享代码,但是导入到的每个库
每个应用程序都是特定于该应用程序的。应用程序可以从磁盘存储和获取状态,但仅限于特定位置
这意味着应用程序也不能通过磁盘进行交互。为了便于相互应用的通信,scarpet托管了它的
拥有在主机上当前运行的所有应用程序之间共享的键值存储,提供获取
与可选设置相关联的值(如果不存在),以及修改系统内容的操作
全球价值。
 
### `system_variable_get(key, default_value ?)`
 
返回从系统共享键值存储中键入`key`值的变量(如果值为
不存在,并且提供了默认表达式,设置要与该键关联的新值
 
### `system_variable_set(key, new_value)`
 
从系统共享键值存储中返回用`key`值键入的变量,并设置新的
键循环和高阶函数的映射
 
有效地使用这些函数可以大大简化您的程序并加快它们的速度,正如这些函数所做的那样
将需要同时应用于多个值的大多数操作内部化。他们中的大多数人
一个`list`参数,可以是scarpet中的任何iterable结构,包括生成器,如`rect`或`range`,
和映射,其中迭代器返回所有映射键
 
## 循环
 
### `break(), break(expr), continue(), continue(expr)`
 
它们允许控制循环的执行,或者跳过当前的迭代代码,使用`continue`,或者完成
当前循环,使用`break`。`break`和`continue`只能在`for`,`c_for`,`while`,`loop`,`map`,
`filter`,`reduce`以及Minecraft API块循环,`scan`和`volume`
函数,而`break`也可以用在`first`中。在这些函数的内部表达式之外,
调用`break`或`continue`将导致错误。对于嵌套循环和更复杂的设置,请使用
自定义`try`和`throw`设置。
 
请检查相应的循环函数说明`continue`和`break`在其上下文中的作用,但在
一般情况下,传递给`break`和`continue`的值将用于替换内部
迭代表达式。
 
### `c_for(init, condition, increment, body)`
 
`c_for `模仿c样式的tri-arg(加上body)for循环。返回值`c_for`是在
循环。与其他循环不同的是,`body`没有提供预初始化的``样式变量-所有初始化
增量必须由程序员自己处理。
`break`和continue`语句只能在body表达式中处理,不能在condition或increment中处理。
 
<pre>
 c_for(x=0, x<10, x+=1,
    c_for(y=0, y<10, y+=1,
        print(str('%d * %d = %d', x, y, x*y))
    )
 )
</pre>
 
### `for(list,expr(_,_i))`
 
对`list`中的项列表计算表达式。向`expr`提供`_`(值)和``_i`(迭代次数)。
 
返回`expr`成功的次数。使用`continue`和`break`参数代替返回的
值,以确定迭代是否成功。
 
<pre>
check_prime(n) -> !first( range(2, sqrt(n)+1), !(n % _) );
for(range(1000000,1100000),check_prime(_))  => 7216
</pre>
 
从中我们可以知道在1M到1.1M之间有7216个素数
 
### `while(cond, limit, expr)`
 
重复计算表达式`expr`,直到条件`cond`变为false,但不超过`limit`次。
返回上次`expr`求值的结果,如果没有成功的结果,则返回`null`。`expr`和`cond`都将
接收到一个表示当前迭代的绑定变量``所以它是一个数字。
 
<pre>
while(a<100,10,a=_*_)  => 81 // loop exhausted via limit
while(a<100,20,a=_*_)  => 100 // loop stopped at condition, but a has already been assigned
while(_*_<100,20,a=_*_)  => 81 // loop stopped at condition, before a was assigned a value
</pre>
 
### `loop(num,expr(_),exit(_)?)`
 
计算表达式`expr`,`num`的次数。code>expr接收表示迭代的``系统变量。
 
<pre>
loop(5, game_tick())  => repeat tick 5 times
list = l(); loop(5, x = _; loop(5, list += l(x, _) ) ); list
  // double loop, produces: [[0, 0], [0, 1], [0, 2], [0, 3], [0, 4], [1, 0], [1, 1], ... , [4, 2], [4, 3], [4, 4]]
</pre>
 
在这个小例子中,我们将搜索前10个素数,显然包括0：
 
<pre>
check_prime(n) -> !first( range(2, sqrt(n)+1), !(n % _) );
primes = l();
loop(10000, if(check_prime(_), primes += _ ; if (length(primes) >= 10, break())));
primes
// outputs: [0, 1, 2, 3, 5, 7, 11, 13, 17, 19]
</pre>
 
## 高阶函数
 
### `map(list,expr(_,_i))`
 
将值的`list`转换为另一个列表,其中每个值都是表达式`v=expr(u,\i)`的结果
其中`_`作为列表的每个元素传递,`_i`是该元素的索引。如果`break`被称为
地图返回到目前为止收集到的任何东西。如果`continue`和`break`与提供的参数一起使用,则在
结果地图元素的位置,否则跳过当前元素。
 
<pre>
map(range(10), _*_)  => [0, 1, 4, 9, 16, 25, 36, 49, 64, 81]
map(player('*'), _+' is stoopid') [gnembon is stoopid, herobrine is stoopid]
</pre>
 
### `filter(list,expr(_,_i))`
 
筛选器`list`元素只返回返回`expr`的正结果。带`break`和`continue`
语句中,提供的值可以用作布尔检查。
 
<pre>
filter(range(100), !(_%5), _*_>1000)  => [0, 5, 10, 15, 20, 25, 30]
map(filter(entity_list('*'),_=='Witch'), query(_,'pos') )  => [[1082.5, 57, 1243.5]]
</pre>
 
### `first(list,expr(_,_i))`
 
查找并返回列表中满足`expr`的第一项。它为当前元素值设置`_`,
`_i`表示该元素的索引`break`可以在迭代代码中使用其参数值进行调用
而不是当前项`continue`没有意义,不能在 `first` 调用中调用。
 
<pre>
first(range(1000,10000), n=_; !first( range(2, sqrt(n)+1), !(n % _) ) )  => 1009 // first prime after 1000
</pre>
 
注意,在上面的示例中,我们需要将外部的`_`重命名为albe,以便在内部的`first`调用中使用
 
### `all(list,expr(_,_i))`
 
如果列表中的所有元素都满足条件,则返回`true`。大致相当
to`all(list,expr) <=> for(list,expr)==length(list)``expr`还接收绑定的`\`和`\`变量`中断`
和`continue`没有意义,不能在`expr`正文中使用。
 
<pre>
all([1,2,3], check_prime(_))  => true
all(neighbours(x,y,z), _=='stone')  => true // if all neighbours of [x, y, z] are stone
map(filter(rect(0,4,0,1000,0,1000), [x,y,z]=pos(_); all(rect(x,y,z,1,0,1),_=='bedrock') ), pos(_) )
  => [[-298, 4, -703], [-287, 4, -156], [-269, 4, 104], [242, 4, 250], [-159, 4, 335], [-208, 4, 416], [-510, 4, 546], [376, 4, 806]]
    // find all 3x3 bedrock structures in the top bedrock layer
map( filter( rect(0,4,0,1000,1,1000,1000,0,1000), [x,y,z]=pos(_);
        all(rect(x,y,z,1,0,1),_=='bedrock') && for(rect(x,y-1,z,1,1,1,1,0,1),_=='bedrock')<8),
   pos(_) )  => [[343, 3, -642], [153, 3, -285], [674, 3, 167], [-710, 3, 398]]
    // ditto, but requiring at most 7 bedrock block in the 18 blocks below them
</pre>
 
### `reduce(list,expr(_a,_,_i), initial)`
 
对列表的每个元素应用`expr`,并将结果保存在`a`累加器中。连续调用`expr`
可以访问该值以应用更多值。您还需要指定初始值以应用
蓄能器`break`可用于提前终止reduction。如果为`break`或`continue`提供了值,
从现在起,它将用作累加器的新值。
 
<pre>
reduce([1,2,3,4],_a+_,0)  => 10
reduce([1,2,3,4],_a*_,1)  => 24
</pre>
 
# 自定义函数和程序控制流程
 
## 编写多于一行的程序
 
### `Operator ;`
 
为了有效地编写不止一行的程序,程序员需要指定命令序列的方法
一个接一个地执行。在`scarpet`中,这可以通过`;`实现。它是一个操作符,通过分离
带分号的语句。既然空白和
`$`(命令行可见换行符)
符号都被视为空白,你如何布局你的代码并不重要,只要它是可读的每个人参与。
 
<pre>
expr;
expr;
expr;
expr
</pre>
 
请注意,最后一个表达式后面没有分号。因为指令分离是功能性的
在`scarpet`中,而不仅仅是一个指令分隔符,用悬空运算符终止代码是不可能的
必须有效。话虽如此,由于许多编程语言并不关心操作终止符的数量
程序员使用,预处理器将删除所有不必要的分号从脚本编译时。
 
一般来说`表达式; 表达式; 表达式; 表达式`相当于`(((表达式;表达式);表达式);表达式)`。
 
计算表达式的结果与第二个表达式的结果相同,但第一个表达式
还评估了副作用
 
<pre>
expr1 ; expr2 => expr2  // with expr1 as a side-effect
</pre>
 
## 全局变量
 
所有定义的函数都被编译,持久存储,并在应用程序中全局可用。
函数只能通过调用`undef('fun')`来取消定义,这将删除函数`fun`的全局项。
由于所有变量在每个函数或每个命令脚本中都有局部作用域,
全局变量是一种共享全局状态的方法。
 
任何名称以`global\`开头的变量都将被全局存储和访问,而不仅仅是
在当前范围内。如果直接在聊天窗口中使用默认应用程序,它将在对`/script的调用中保持不变`
功能。与全局函数一样,全局变量只能通过`undef`来定义。
 
对于在`global`范围内运行的应用程序-所有播放器将共享相同的全局变量和定义的函数,
在`player`范围内,每个播放器为每个应用程序托管自己的状态,因此函数和全局变量是不同的。
 
 
<pre>
/script run a() -> global_list+=1; global_list = l(1,2,3); a(); a(); global_list  // => [1, 2, 3, 1, 1]
/script run a(); a(); global_list  // => [1, 2, 3, 1, 1, 1, 1]
</pre>
 
### `运算符 ->`
 
`->`运算符有两种用途-作为函数定义运算符和映射的键值初始值设定项。
 
为了比简单的操作序列更好地组织代码,可以定义函数。定义正确
如果有以下形式
 
<pre>
fun(args, ...) -> expr
</pre>
 
其中`fun(args,…)`是一个函数签名,表示函数名,参数数及其名称,
expr是一个表达式(可以是复杂的),在调用`fun`时进行计算。签名里的名字不是
如果需要在其他任何地方使用,这些名称的其他出现将在此函数作用域中被屏蔽。功能
call为`expr`中的变量创建新的作用域,因此所有非全局变量在调用者中都不可见
范围。所有参数都按值传递给新范围,包括列表和其他容器,但是它们的
抄袭会很肤浅。
 
函数将自身作为第一类对象返回,这意味着以后可以使用`call`函数调用它
 
使用`_`作为函数名会创建匿名函数,因此每次定义``函数时,都会给出它
唯一的名称,您可以将其传递到其他地方以获取此函数的`call`。只能调用匿名函数
通过它们的值和`call`方法。
 
<pre>
a(lst) -> lst+=1; list = l(1,2,3); a(list); a(list); list  // => [1,2,3]
</pre>
 
如果内部函数想要操作和修改更大的对象,则从外部范围列出列表,但不是全局的,
它需要在函数签名中使用`outer`函数。
 
在映射构造上下文中(直接在`m()`或`{}`中),`->`运算符通过转换其
映射构造函数用作键值对的元组的参数：
 
<pre>
{ 'foo' -> 'bar' } => {l('foo', 'bar')}
</pre>
 
这意味着不可能从字面上定义一组内联函数,但是一组函数仍然可以
通过将元素添加到一个空集,并以这种方式构建它来创建。这是一个折衷有一个很酷的地图初始化。
 
### `outer(arg)`
 
`outer`函数只能在函数签名中使用,它会在其他任何地方导致错误。它
保存外部作用域中该变量的值,并允许在内部作用域中使用它。这是一个类似的例子
在Java的lambda函数定义中使用外部变量的行为,除了这里您必须指定
要使用哪些变量,以及借用哪些变量
 
这种机制可以用来使用静态可变对象,而不需要使用`global_...`
 
<pre>
list = l(1,2,3); a(outer(list)) -> list+=1;  a(); a(); list  // => [1,2,3,1,1]
</pre>
 
函数的返回值是最后一个表达式的值。这与使用外部或
全球名单,但更昂贵
 
<pre>
a(lst) -> lst+=1; list = l(1,2,3); list=a(list); list=a(list); list  // => [1,2,3,1,1]
</pre>
 
能够将多个语句组合到一个表达式中,包括函数,传递参数以及全局和外部表达式
作用域允许组织更大的脚本
 
### `运算符 ...`
 
定义一个函数参数来表示剩余参数的可变长度参数列表
从参数调用列表,也称为varargs。函数签名中只能有一个已定义的vararg参数。
从技术上讲,它在哪里并不重要,但它最好看它屁股的一面。
 
<pre>
foo(a, b, c) -> ...  # fixed argument function, call foo(1, 2, 3)
foo(a, b, ... c) -> ... # c is now representing the variable argument part
    foo(1, 2)  # a:1, b:2, c:[]
    foo(1, 2, 3)  # a:1, b:2, c:[3]
    foo(1, 2, 3, 4)  # a:1, b:2, c:[3, 4] 
foo(... x) -> ...  # all arguments for foo are included in the list
</pre>
 
### `import(module_name, symbols ...)`
 
将符号从其他应用程序和库导入当前应用程序和库：全局变量或函数,允许使用
在当前应用程序中删除它们。这包括这些模块导入的其他符号。Scarpet支持循环依赖,
但是如果符号直接用在模块体中而不是函数中,它可能无法检索它们。
返回可从此模块导入的可用符号的完整列表,可用于调试导入
问题,并列出库的内容。
 
### `call(function, args.....)`
 
使用指定的参数调用用户定义的函数。它相当于直接调用`function(args…)`
但是你可以用函数值,或者名字代替它。这意味着您可以将函数传递给其他用户定义的
函数作为参数并在内部使用`call`调用它们。因为函数定义返回定义的
函数,它们可以就地定义为匿名函数。
 
#### 将函数引用传递给应用程序的其他模块
 
如果函数是由其名称定义的,Scarpet将尝试在给定模块及其导入中解析其定义,
这意味着如果调用在导入的库中,而不是在应用程序的主模块中,并且从
图书馆的观点,但在应用程序中,它将无法调用。如果你把一个函数名传递给应用程序中的一个单独模块,
它应该从主模块中导入该方法以获得可见性。
 
检查一个库的有问题代码的示例,该库希望函数值作为传递的参数,以及如何调用它
父应用程序：
```
//app.sc
import('lib', 'callme');
foo(x) -> x*x;
test() -> callme('foo' , 5);
```
```
//lib.scl
callme(fun, arg) -> call(fun, arg);
```
 
在这种情况下,`foo`将无法在`lib`中取消引用,因为它在名称上不可见。在紧耦合模块中,其中`lib`只是
作为`app`的一个组件,您可以使用循环导入来确认来自另一个模块的符号(非常类似于
在Java类中导入),这解决了问题,但使库依赖于主应用程序：
```
//lib.scl
import('app','foo');
callme(fun, arg) -> call(fun, arg);
```
您可以通过显式地取消引用本地函数来避免这个问题,该函数用作创建的lambda参数
在所请求函数可见的模块中：
```
//app.sc
import('lib', 'callme');
foo(x) -> x*x;
test() -> callme(_(x) -> foo(x), 5);
```
```
//lib.scl
callme(fun, arg) -> call(fun, arg);
```
或者通过向函数传递显式引用,而不是通过名称调用函数：
```
//app.sc
import('lib', 'callme');
global_foohandler = (foo(x) -> x*x);
test() -> callme(global_foohandler, 5);
```
 
一点技术上的注意：由于没有`_`
为每个被调用的函数创建新的调用堆栈,但是匿名函数是唯一可用的机制
有自己lambda参数的程序员
 
<pre>
my_map(list, function) -> map(list, call(function, _));
my_map(l(1,2,3), _(x) -> x*x);    // => [1,4,9]
profile_expr(my_map(l(1,2,3), _(x) -> x*x));   // => ~32000
sq(x) -> x*x; profile_expr(my_map(l(1,2,3), 'sq'));   // => ~36000
sq = (_(x) -> x*x); profile_expr(my_map(l(1,2,3), sq));   // => ~36000
profile_expr(map(l(1,2,3), _*_));   // => ~80000
</pre>
 
## 控制流
 
### `return(expr?)`
 
有时打破有组织的控制流是很方便的,或者传递的最终结果值是不实际的
最后一个语句的函数,在这种情况下可以使用return语句
 
如果未提供参数-返回空值。
 
<pre>
def() -> (
   expr1;
   expr2;
   return(expr3); // function terminates returning expr3
   expr4;     // skipped
   expr5      // skipped
)
</pre>
 
一般来说,将最后一个表达式保留为返回值比调用
到处返回,但这往往会导致代码混乱。
 
### `exit(expr?)`
 
作为程序执行的结果,它终止传递`expr`的整个程序,如果省略,则终止null。
 
### `try(expr)` `try(expr, user_catch_expr)` `try(expr, type, catch_expr, type?, catch_expr?, ...)`
 
`try`expression,允许捕获将在`expr`语句中抛出的异常。例外情况可以是
使用`throw()`显式抛出,或在scarpet内部抛出,其中代码正确但检测到非法状态。二元形式
只捕获用户抛出的异常,一个参数调用`try(expr)`等同于`try(expr,null)`,
或`try(expr,`user exception`,null)`。如果定义了多个`type catch`对,则在第一个对上终止执行
引发的异常的适用类型。因此,即使捕获的异常匹配多个筛选器,也只有
将执行第一个匹配块。
 
Catch表达式用
`_`设置为与异常关联的值,并将` trace`设置为包含有关错误点的详细信息(标记,行和行)
列位置),调用堆栈和本地
故障时的变量。`type`将捕获该类型的任何异常以及该类型的任何子类型。
  
 
您可以使用`try`机制从复杂的调用堆栈的大部分中退出,并继续执行程序
与不抛出异常相比,异常通常要昂贵得多。
 
`try`函数允许您捕获一些包含无效数据的scarpet异常,例如invalid
数据包,资源包或其他mod修改的块,生物群落,尺寸和其他东西,
或者当错误超出程序员的范围时,例如读取或解码文件时出现问题。
 
这是可以使用`try`函数在中抛出/捕获的异常的层次结构：
-`exception`：这是基本异常。捕捉`异常`可以捕捉到所有可以捕捉到的东西,
但和其他地方一样,这样做听起来是个坏主意。
-`value exception`：这是由于
提供给内置函数的参数值不正确
-`未知的项目`,`未知的块`,`未知的生物群落`,`未知的声音`,`未知的粒子`,
`未知的 poi 类型`,`未知的维度`,`未知的结构`,`未知的标准`：特定
指定的内部名称不存在或无效时引发的错误。
-`io_exception`：这是由于处理外部数据时出错而发生的任何异常的父级。
-`nbt error`：输入/输出nbt文件不正确。
-`json\错误`：输入/输出json数据不正确。
-`b64 error`：不正确的输入/输出b64(base 64)字符串
-`user exception`：默认情况下使用`throw`函数引发的异常。
  
简介：
<pre>
inner_call() ->
(
   aaa = 'booyah';
   try(
      for (range(10), item_tags('stick'+_*'k'));
   ,
      print(_trace) // not caught, only catching user_exceptions
   )
);

outer_call() -> 
( 
   try(
      inner_call()
   , 'exception', // catching everything
      print(_trace)
   ) 
);
</pre>
输出：
```
{stack: [[<app>, inner_call, 1, 14]], locals: {_a: 0, aaa: booyah, _: 1, _y: 0, _i: 1, _x: 0, _z: 0}, token: [item_tags, 5, 23]}
```
 
### `throw(value?)`, `throw(type, value)`, `throw(subtype, type, value)`
 
抛出可以在`try`块中捕获的异常(见上文)。如果在没有参数的情况下运行,它将抛出`user exception`
将`null`作为值传递给`catch expr`。通过两个参数,您可以模拟scarpet中抛出的任何其他异常类型。
使用3个参数,您可以指定一个自定义异常作为提供的`type`的`subtype`,允许自定义`try`
包含自定义异常的语句。
 
### `if(cond, expr, cond?, expr?, ..., default?)`
 
If语句是一个函数,它接受一个接一个地求值的多个条件,如果
如果结果为true,则返回其`expr`,否则,如果所有条件都失败,则返回值为`default`
表达式,或`null`(如果跳过默认值)
 
`if`函数等价于`if(cond)expr;else if(cond)表达式;else默认值;`来自爪哇,
只是函数形式
# 对容器的列表,映射和API支持
 
Scarpet支持基本的容器类型：列表和映射(aka hashmaps,dicts等等)
 
## 容器操作
 
这是一份适用于所有类型容器的操作列表：列表,地图以及其他特定于地雷的操作
可修改的容器,如NBT
 
### `get(container, address, ...), get(lvalue), ':' operator`
 
从`value`返回`address`元素处的值。对于表示索引的列表,使用负数
从列表末尾到达元素`get`call将始终能够找到索引。万一没什么
项目,它将循环
 
对于映射,检索`address`中指定的键下的值,否则为null
 
[Minecraft-specific usecase]：如果`value`是`nbt`类型,则使用address作为nbt路径进行查询,返回null,
如果找不到路径,则为一个值(如果有一个匹配项),或者为值列表(如果结果是列表)。返回的元素可以
可以是数字类型,字符串文本或其他复合nbt标记
 
为了简化对嵌套对象的访问,可以将地址链添加到`get`的参数中
而不是多次打电话`get(get(foo,a),b)`等价于get(foo,a,b)`,或`foo:a：b`。
 
<pre>
get([range(10)], 5)  => 5
get([range(10)], -1)  => 9
get([range(10)], 10)  => 0
[range(10)]:93  => 3
get(player() ~ 'nbt', 'Health') => 20 // inefficient way to get player health, use player() ~ 'health' instead
get({ 'foo' -> 2, 'bar' -> 3, 'baz' -> 4 }, 'bar')  => 3
</pre>
 
### `has(container, address, ...), has(lvalue)`
 
类似于`get`,但返回布尔值,指示给定的索引/键/路径是否在容器中。
可用于确定`get(…)==null`表示元素不存在,还是表示此元素的存储值不存在
地址为`null`,运行起来比`get`便宜。
 
与get一样,它可以接受嵌套容器中链的多个地址。在本例中`has(foo:a：b)`is
相当于`has(get(foo,a),b)`或`has(foo,a,b)``
 
### `delete(container, address, ...), delete(lvalue)`
 
从容器中删除特定条目。对于列表-删除元素并收缩它。对于地图,它
从映射中删除键,对于nbt-从给定路径中删除内容。对于列表和地图返回上一个
地址中的条目,用于nbt—删除的对象数,0表示原始值未受影响。
 
与`get`和`has`一样,`delete`可以接受链接地址,也可以接受l值容器访问
所提供路径的叶中的值,因此`delete(foo,a,b)`是
与`delete(get(foo,a),b)`或`delete`相同(foo:a：b)`
 
如果容器已更改,则返回true;如果容器保持不变,则返回false;如果操作无效,则返回null。
 
### `put(容器,地址,值),put(容器,地址,值,模式),put(左值,值)`
 
<u> **列表**</u>
 
通过将地址下的值替换为提供的`value`来修改容器。对于列表,有效的
索引是必需的,但也可以是负数,以指示列表末尾的位置。如果`null`为
作为地址提供,它总是意味着-添加到列表的末尾。
 
有三种模式可以将项目添加到列表中：
 
*`replace`(默认)：替换给定索引(地址)下的项。不会更改数组的大小
除非使用`null`地址,这是一个例外,然后它会附加到结尾
*`insert`：在指定的索引处插入给定的元素,移动数组的其余部分为该项腾出空间。
注意,索引-1指向列表的最后一个元素,因此在该位置插入并移动前一个元素
最后一个元素到新的最后一个元素位置。要在末尾插入,请在put中使用`+=`运算符或`null`地址
*`extend`：将提供的值视为一组可iterable值,以在给定索引处插入,从而扩展列表
按此数量的项目。再次使用`null`地址/索引指向列表的末尾
 
由于有额外的mode参数,`put`没有链接,但您仍然可以使用l值容器访问
表示容器和地址,因此`put(foo,key,value)`与`put`相同(foo:key,值)`or`foo:key=value`
 
如果修改了容器,则返回true;否则返回false;如果操作无效,则返回null。
 
<u> **地图**</u>
 
对于地图,没有可用的模式(然而,似乎没有理由这样做)。它将替换所提供的
键(地址),如果当前不存在则设置它。
 
<u> **NBT标签**</u>
 
nbt值的地址是将与`/data`命令一起使用的有效nbt路径,tag是
将适用于给定的插入操作。请注意,要区分正确的类型(如整数类型,
您需要使用命令表示法,即常规int是`123`,而字节大小int是`123b`,并且是显式的
字符串应该是`5`,所以scarpet在字符串中使用单引号很有帮助。与列表和地图不同,它
返回受影响的节点数,如果没有受影响,则返回0。
 
NBT标签有三种添加项目的模式：
 
*`replace`(默认)：替换给定路径(地址)下的项。如果可能,先删除它们,然后添加给定的
元素到提供的位置。目标路径可以指示复合标记键,列表或单个元素
名单的一部分。
*`<N>`：列表插入的索引。在用
路径地址。如果未指定列表,则失败。它的行为类似于列表的`插入`模式,即不删除任何
现有元素的。使用`replace`删除和替换现有元素。
*`merge`：假设path和replacement目标都是复合类型(字典,映射,`{}`类型),
并将`value`中的键与路径下的复合标记合并
 
<pre>
a = [1, 2, 3]; put(a, 1, 4); a  => [1, 4, 3]
a = [1, 2, 3]; put(a, null, 4); a  => [1, 2, 3, 4]
a = [1, 2, 3]; put(a, 1, 4, 'insert'); a  => [1, 4, 2, 3]
a = [1, 2, 3]; put(a, null, [4, 5, 6], 'extend'); a  => [1, 2, 3, 4, 5, 6]
a = [1, 2, 3]; put(a, 1, [4, 5, 6], 'extend'); a  => [1, 4, 5, 6, 2, 3]
a = [[0,0,0],[0,0,0],[0,0,0]]; put(a:1, 1, 1); a  => [[0, 0, 0], [0, 1, 0], [0, 0, 0]]
a = {1,2,3,4}; put(a, 5, null); a  => {1: null, 2: null, 3: null, 4: null, 5: null}
tag = nbt('{}'); put(tag, 'BlockData.Properties', '[1,2,3,4]'); tag  => {BlockData:{Properties:[1,2,3,4]}}
tag = nbt('{a:[{lvl:3},{lvl:5},{lvl:2}]}'); put(tag, 'a[].lvl', 1); tag  => {a:[{lvl:1},{lvl:1},{lvl:1}]}
tag = nbt('{a:[{lvl:[1,2,3]},{lvl:[3,2,1]},{lvl:[4,5,6]}]}'); put(tag, 'a[].lvl', 1, 2); tag
     => {a:[{lvl:[1,2,1,3]},{lvl:[3,2,1,1]},{lvl:[4,5,1,6]}]}
tag = nbt('{a:[{lvl:[1,2,3]},{lvl:[3,2,1]},{lvl:[4,5,6]}]}'); put(tag, 'a[].lvl[1]', 1); tag
     => {a:[{lvl:[1,1,3]},{lvl:[3,1,1]},{lvl:[4,1,6]}]}
</pre>
 
## 列表操作
 
### `[value,…?]`,`[iterator]`,`l(value,…?`,`l(iterator))`
 
创建作为参数传递的表达式的值列表。它可以用作L值,如果所有
元素是变量,您可以使用它从一个函数调用返回多个结果,如果
函数返回与`[]`调用使用的大小相同的结果列表。万一只有一个
参数,它是一个迭代器(vanilla expression specification有`range`,但Minecraft API实现了
一堆,比如`diamond`),它会把它转换成一个合适的列表。迭代器只能用于高阶
函数和被视为空列表,除非用`[]`展开。
 
在内部,`[elem,…]`(列表语法)和`l(elem,…)`(函数语法)是等价的简单地翻译成
`l()`scarpet预处理阶段。这意味着在代码内部,尽管有`[]`
不使用不同类型的括号,也不是合适的运算符。这意味着`l(]`和`[)`也是有效的
尽管不推荐,因为它们会使代码的可读性大大降低。
 
<pre>
l(1,2,'foo') <=> [1, 2, 'foo']
l() <=> [] (empty list)
[range(10)] => [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]
[1, 2] = [3, 4] => Error: l is not a variable
[foo, bar] = [3, 4]; foo==3 && bar==4 => 1
[foo, bar, baz] = [2, 4, 6]; [min(foo, bar), baz] = [3, 5]; [foo, bar, baz]  => [3, 4, 5]
</pre>
 
在上一个示例中,`[min(foo,bar),baz]`创建了一个有效的L值,因为`min(foo,bar)`找到了
变量(在本例中为`foo`)创建一个有效的可赋值L列表,其中包含`[foo,baz]`,以及这些值
将被指定新值
 
### `join(delim, list), join(delim, values ...)`
 
返回包含列表,迭代器或所有值的联接元素的字符串,
与`delim`分隔符串联
 
<pre>
join('-',range(10))  => 0-1-2-3-4-5-6-7-8-9
join('-','foo')  => foo
join('-', 'foo', 'bar')  => foo-bar
</pre>
 
### `split(delim?, expr)`
 
按`delim`拆分`expr`下的字符串,delim可以是正则表达式。如果没有指定分隔符,它将被拆分
按字符。
 
如果`expr`是一个列表,它将按等于`delim`或等于空字符串的元素将列表拆分为多个子列表
如果没有指定分隔符。
 
拆分`null`值将返回空列表。
 
<pre>
split('foo') => [f, o, o]
split('','foo')  => [f, o, o]
split('.','foo.bar')  => []
split('\\.','foo.bar')  => [foo, bar]
split(1,[2,5,1,2,3,1,5,6]) => [[2,5],[2,3],[5,6]]
split(1,[1,2,3,1,4,5,1] => [[], [2,3], [4,5], []]
split(null) => []
</pre>
 
### `slice(expr, from, to?)`
 
提取子字符串或子列表(基于expr下表达式的结果类型)
起始索引为`from`,如果提供,则以`to`结尾,如果省略,则以`end`结尾。可以使用负指数
从列表后面指示计数,因此`-1<=>长度(216;)`。
 
迭代器(`range`,`rect`等)有一个特殊情况,它需要非负索引(负的`from`被视为
`0`,并将负数`to`作为`inf`),但允许检索部分序列并忽略
其他部分。在这种情况下,对`slice`的连续调用将引用索引 `0` ,即自迭代器以来的当前迭代位置
无法返回或跟踪序列中的位置(参见示例)。
 
<pre>
slice([0,1,2,3,4,5], 1, 3)  => [1, 2]
slice('foobar', 0, 1)  => 'f'
slice('foobar', 3)  => 'bar'
slice(range(10), 3, 5)  => [3, 4]
slice(range(10), 5)  => [5, 6, 7, 8, 9]
r = range(100); [slice(r, 5, 7), slice(r, 1, 3)]  => [[5, 6], [8, 9]]
</pre>
 
### `sort(list), sort(values ...)`
 
按默认的排序顺序对所有参数排序,如果只有一个参数,则按列表排序。
它返回一个新的排序列表,不影响传递给参数的列表
 
<pre>
sort(3,2,1)  => [1, 2, 3]
sort('a',3,11,1)  => [1, 3, 11, 'a']
list = l(4,3,2,1); sort(list)  => [1, 2, 3, 4]
</pre>
 
### `sort_key(list, key_expr)`
 
按`key_expr`为每个元素定义的一个或多个键对列表副本进行排序
 
<pre>
sort_key([1,3,2],_)  => [1, 2, 3]
sort_key([1,3,2],-_)  => [3, 2, 1]
sort_key(l(range(10)),rand(1))  => [1, 0, 9, 6, 8, 2, 4, 5, 7, 3]
sort_key(l(range(20)),str(_))  => [0, 1, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 2, 3, 4, 5, 6, 7, 8, 9]
</pre>
 
### `range(to), range(from, to), range(from, to, step)`
 
创建一个从`from`到`to`的数字范围。`step`参数不仅指示
增量大小,但也可以是方向(可以是负数)。返回的值不是正确的列表,只是迭代器
但是,如果出于任何原因,您需要一个包含所有评估项的正确列表,请使用`l(range(to))``。
主要用于高阶函数
 
<pre>
range(10)  => [...]
[range(10)]  => [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]
map(range(10),_*_)  => [0, 1, 4, 9, 16, 25, 36, 49, 64, 81]
reduce(range(10),_a+_, 0)  => 45
range(5,10)  => [5, 6, 7, 8, 9]
range(20, 10, -2)  => [20, 18, 16, 14, 12]
range(-0.3, 0.3, 0.1)  => [-0.3, -0.2, -0.1, 0, 0.1, 0.2]
range(0.3, -0.3, -0.1) => [0.3, 0.2, 0.1, -0, -0.1, -0.2]
</pre>
 
## 地图操作
 
Scarpet支持map结构,aka hashmaps,dicts等。map结构也可以使用,`null`值作为集合。
除了容器访问功能外,(`,get,put,has,delete`),包括以下函数：
 
### `{values, ...}`,`{iterator}`,`{key -> value, ...}`,`m(values, ...)`, `m(iterator)`, `m(l(key, value), ...))`
 
使用提供的键和值创建并初始化映射。如果参数包含一个平面列表,则这些都是
作为没有值的键处理,迭代器也是如此-创建一个行为类似于集合的映射。如果
参数是一个列表,每个列表必须有两个元素,第一个是键,第二个是值
 
在映射创建上下文中(直接在`{}`或` m{}`调用中),`->`运算符的作用类似于对构造函数,以实现更简单的映射
语法提供键值对,因此对`{foo->bar,baz->qux}`的调用相当于
`{l(foo,bar),l(baz,qux)}`,相当于更古老但更传统的函数形式
`m(l(foo,bar),l(baz,quoz))`。
 
在内部,`{?}`(列表语法)和`m(?)`(函数语法)是等价的简单地翻译成
`m()`scarpet预处理阶段。这意味着在内部,尽管`{},代码始终使用表达式语法`
不使用不同类型的括号,也不是合适的运算符。这意味着`m(}`和`{)`也是有效的
尽管不推荐,因为它们会使代码的可读性大大降低。
 
在将映射值转换为字符串时,``：``用作键值分隔符,因为它与NBT暂时兼容
表示法,这意味着在更简单的情况下,可以通过调用`str()`,将映射转换为NBT可解析字符串。然而,这
不保证可分析的输出。要正确转换为NBT值,请使用`encode NBT()``。
 
<pre>
{1, 2, 'foo'} => {1: null, 2: null, foo: null}
m() <=> {} (empty map)
{range(10)} => {0: null, 1: null, 2: null, 3: null, 4: null, 5: null, 6: null, 7: null, 8: null, 9: null}
m(l(1, 2), l(3, 4)) <=> {1 -> 2, 3 -> 4} => {1: 2, 3: 4}
reduce(range(10), put(_a, _, _*_); _a, {})
     => {0: 0, 1: 1, 2: 4, 3: 9, 4: 16, 5: 25, 6: 36, 7: 49, 8: 64, 9: 81}
</pre>
 
### `keys(map), values(map), pairs(map)`
 
返回映射中所有项的键,值和键值对的完整列表(2元素列表)

# Minecraft特定API和`scarpet`语言加载项和命令
 
下面是有关Minecraft的函数列表，如果没有他们，CarpetScrpt就可以不依赖于Minecraft运行了
 
 
## 应用程序结构
 
scarpet程序在游戏中的主要交付方式是以应用程序的形式出现在`*.sc`文件中,这些文件位于`脚本`中
文件夹,扁平。在singleplayer中,您还可以将应用程序保存在`.minecraft/config/carpet/scripts`中,以便在任何世界中都可以使用,
在这里你可以把它们组织在文件夹里。
加载时(通过`/script load`命令等),游戏将运行一次应用程序的内容,而不管其范围如何
(下面是关于应用程序作用域的更多信息),不执行任何函数,除非直接调用,并且
`__config()`函数,如果存在,将执行一次。加载应用程序也会绑定特定的
事件到事件系统(有关详细信息,请查看事件部分)。
 
如果应用程序定义了` on  start()`函数,则在运行其他任何操作之前,它将执行一次。对于全局范围的应用程序,
这是在加载之后,对于播放器范围内的应用程序,在播放器第一次使用它们之前。
与静态代码(直接写在应用程序代码体中)不同,静态代码总是在每个应用程序中运行一次,如果
这是一个播放器应用程序,服务器上有多个播放器。
 
卸载应用程序会从游戏中移除其所有状态,禁用命令,移除绑定事件,以及
保存其全局状态。如果需要更多的清理,可以定义` on close()`函数
在卸载模块,服务器关闭或崩溃时执行。然而,没有必要这样做
如前一条语句所示,显式地用于自动清理的对象。具有`global`作用域
应用程序` on close()`将在每个应用程序中执行一次,而使用`player`作用域应用程序时,将在每个应用程序中对每个播放器执行一次。
 
###App config via`u config()`函数
 
如果应用程序定义了` config`方法,并且该方法返回一个映射,则将使用该方法应用自定义设置
对于此应用程序。目前支持以下选项：
 
*``scope``：应用程序的全局变量的默认范围,默认值为``player``,这意味着globals和defined
每个播放器的功能将是唯一的,这样每个播放器的应用程序将独立运行。这在某些情况下很有用
类似工具的应用程序,总是从玩家的角度来看事物的行为。初次跑步时
应用程序创建的是初始状态：已定义的函数,全局变量,配置和事件处理程序,然后将这些状态复制到
与应用程序交互的每个播放器。`global`作用域-初始加载创建的状态是
应用程序状态和所有玩家交互在同一上下文中运行,共享定义的函数,全局参数,配置和事件。
``global``范围最适用于以世界为中心的应用程序,其中要么玩家不相关,要么存储玩家数据
显式键入播放器,播放器名称,uuid等。
即使对于`player`作用域的应用程序,也可以使用命令块访问特定的player应用程序
`/在<app>run…`中执行<player>run脚本。
要访问播放器应用程序的全局/服务器状态,您不应该这样做,您需要拒绝任何播放器的命令,
所以要么使用命令块,要么
任意实体：`/execute as@e[type=bat,limit=1]例如在<app>globals`中运行脚本
在`player`作用域的应用程序的全局作用域中运行任何东西都不是有意的。
*``stay loaded``：默认为`true`。如果为true,并且`/scriptsautooad`已打开,则以下应用程序将
启动后保持加载状态。否则,在第一次读取应用程序并获取配置后,服务器将把它们放下来。
警告：所有应用程序在启动时都将运行一次,因此请注意它们的操作
静态地,将执行一次。只有`脚本`文件夹中存在的应用程序才会自动加载。
*``legacy command type support``—如果`true`,则应用程序通过` command()`函数定义legacy命令系统,
命令函数的所有参数都将使用准将/香草风格的参数解析器及其类型来解释和使用
会从他们的名字推断出来,否则
遗留scarpet变量解析器将用于为命令提供参数。
*``allow command conflicts`-如果定义了自定义应用程序命令树,应用程序引擎将检查并识别
不同执行路径之间的冲突和歧义。虽然准将的命令不明确,
而且他们往往执行正确,建议支持在这种情况下效果很差,而且很糟糕
将警告并阻止此类应用程序加载错误消息。如果指定了`allow command conflicts`,并且
`true`,则scarpet将加载所有提供的命令。
*``requires``-以Fabric的mod.json样式定义mod依赖关系的映射,或者定义要执行的函数。如果是地图,它只会
如果地图中指定的所有mod都符合版本标准,则允许加载应用程序。如果它是一个函数,它将阻止应用程序
如果函数未执行,则加载为`false`,显示返回给用户的内容。
    
版本比较可用的前缀是`>=`,`<=`,`>`,`<`,`~`,`^`和`=`(如果未指定,则为默认值)
at[NPM文档关于SemVer ranges](https://docs.npmjs.com/cli/v6/using-npm/semver#ranges)
    ```
    __config() -> {
      'requires' -> {
        'carpet' -> '>=1.4.33', // Will require Carpet with a version >= 1.4.32
        'minecraft' -> '>=1.16', // Will require Minecraft with a version >= 1.16
        'chat-up' -> '*' // Will require any version of the chat-up mod
      }
    }
    ```
    ```
    __config() -> {
      'requires' -> _() -> (
          d = convert_date(unix_time());
          if(d:6 == 5 && d:2 == 13, 
            'Its Friday, 13th' // Will throw this if Friday 13th, will load else since `if` function returns `null` by default
          )
    }
    ```
*``command permission``-表示运行命令的自定义权限。它可以是一个表示
权限级别(从1到4)或字符串值,其中之一：``all``(默认),``ops``(权限级别为2的默认opped player),
``server`-命令只能通过服务器控制台和commandblocks访问,但不能在聊天中访问,``players`-相反
前者,只允许在玩家聊天时使用。它也可以是函数(lambda函数或函数值,而不是函数名)
它接受1个参数,表示调用的播放器,如果命令表示服务器调用,则接受`null`。
如果命令的计算结果为`false`,则函数将阻止该命令运行。
请注意,Minecraft会在玩家加入或重新加载/重新启动时评估符合条件的命令,因此如果您使用
谓词是易变的,可能会改变,命令可能会错误地执行或不指示它对玩家可用,
然而,玩家将始终能够键入它,或者成功,或者失败,基于他们当前的权限。
自定义权限也适用于具有`legacy command type support`的旧命令
至于用`commands`定义的自定义命令,请参见下文。
*``arguments``-也为带有``legacy command type support``的旧命令定义自定义参数类型
至于用`commands`定义的自定义命令,请参见下文。
*``commands``-定义应用程序的自定义命令,用`/<app>`命令执行,见下文。
 
## 自定义应用程序命令
 
应用程序可以在`/<app>`下注册添加到现有命令系统的自定义命令,其中`<app>`是
应用程序的名称。应用程序提供命令的方式有三种：
 
### 不支持自定义参数的简单命令
 
简介：
```
__command() -> 'root command'
foo() -> 'running foo';
bar(a, b) -> a + b;
baz(a, b) -> // same thing
(
    print(a+b);
    null
)
```
 
如果加载的应用程序包含`__command()`方法,它将尝试用该应用程序名称注册命令,
并以子命令的形式注册应用程序中可用的所有公共函数(不以下划线开头)
`/<app><fun><args…>`。参数是从单个
`贪婪字符串`准将参数,并拆分为函数参数。解析参数是有限的
对于数字,字符串常量和可用的全局变量,用空格分隔。使用函数和运算符而不是
一元`-`是不安全的,所以是不允许的。
在这种模式下,如果函数返回非空值,它将作为结果打印到
调用程序(例如,在聊天中)。如果提供的参数列表与函数的预期参数计数不匹配,则会显示错误消息
将生成。
 
运行不带任何额外参数的app命令,因此`/<app>`将运行` command()->`函数。
 
这种模式最适合那些通常不需要任何参数,并且希望在一个应用程序中公开某些功能的快速应用程序
简单方便的方法。
 
### 支持普通参数类型的简单命令
 
简介：
 ```
__config() -> {'legacy_command_type_support' -> true};
__command() -> print('root command');
foo() -> print('running foo');
add(first_float, other_float) -> print('sum: '+(first_float+other_float)); 
bar(entitytype, item) -> print(entitytype+' likes '+item:0);
baz(entities) -> // same thing
 (
     print(join(',',entities));
 )
 ```
 
它的工作方式类似于auto命令,但参数根据参数获取其推断类型
名称,查看全名或表示变量类型的最后一个``后面的后缀。例如,名为`float`的变量
可以解析为浮点数,但也可以命名为`first float`或`other float`。任何不是
支持,将被解析为`string`类型。
 
参数类型支持包括对自定义参数类型的完全支持(见下文)。
 
### 自定义命令
 
简介。这个例子模拟了vanilla的`effect`命令,添加了额外的参数
提供香草色-可选择从UI隐藏效果图标：
```
__config() -> {'legacy_command_type_support' -> true};
__command() -> print('root command');
foo() -> print('running foo');
add(first_float, other_float) -> print('sum: '+(first_float+other_float)); 
bar(entitytype, item) -> print(entitytype+' likes '+item:0);
baz(entities) -> // same thing
 (
     print(join(',',entities));
 )
 ```

It works similarly to the auto command, but arguments get their inferred types based on the argument
names, looking at the full name, or the suffix after the last `_` that indicates the variable type. For instance, variable named `float` will
be parsed as a floating point number, but it can be named `'first_float'` or `'other_float'` as well. Any variable that is not
supported, will be parsed as a `'string'` type. 

Argument type support includes full support for custom argument types (see below).

### Custom commands

Synopsis. This example mimics vanilla `'effect'` command adding extra parameter that is not 
available in vanilla - to optionally hide effect icon from UI:
```
global_instant_effects = {'instant_health', 'instant_damage', 'saturation'};
__config() -> {
   'commands' -> 
   {
      '' -> _() -> print('this is a root call, does nothing. Just for show'),
      'clear' -> _() -> clear_all([player()]),
      'clear <entities>' -> 'clear_all',
      'clear <entities> <effect>' -> 'clear',
      'give <entities> <effect>' -> ['apply', -1, 0, false, true],
      'give <entities> <effect> <seconds>' -> ['apply', 0, false, true],
      'give <entities> <effect> <seconds> <amplifier>' -> ['apply', false, true],
      'give <entities> <effect> <seconds> <amplifier> <hideParticles>' -> ['apply', true],
      'give <entities> <effect> <seconds> <amplifier> <hideParticles> <showIcon>' -> 'apply',
      
   },
   'arguments' -> {
      'seconds' -> { 'type' -> 'int', 'min' -> 1, 'max' -> 1000000, 'suggest' -> [60]},
      'amplifier' -> { 'type' -> 'int', 'min' -> 0, 'max' -> 255, 'suggest' -> [0]},
      'hideParticles' -> {'type' -> 'bool'}, // pure rename
      'showIcon' -> {'type' -> 'bool'}, // pure rename
   }
};


clear_all(targets) -> for(targets, modify(_, 'effect'));
clear(targets, effect) -> for(targets, modify(_, 'effect', effect));
apply(targets, effect, seconds, amplifier, part, icon) -> 
(
   ticks = if (has(global_instant_effects, effect),  
      if (seconds < 0, 1, seconds),
      if (seconds < 0, 600, 20*seconds)
   );
   for (targets, modify(_, 'effect', effect, ticks, amplifier, part, icon));
)
```
 
这是使用scarpet指定自定义命令的最灵活的方法。它通过提供命令来工作
包含要执行的函数的路径,以及自定义参数类型(可选)。命令列在地图中,其中
键(可以为空)包括
具有命令语法的执行路径,由文本(按原样)和参数(用`<>`包装)组成,并带有名称/后缀
表示其类型的属性的名称,以及表示要调用的函数的值,
定义的函数名,或具有某些默认参数的函数。从命令中提取的值将传递给
功能和执行。默认情况下,将检查命令列表的模糊性(在某个点上具有相同路径的命令)
如果发生这种情况,则会导致应用程序加载错误,但是可以通过指定
``允许命令冲突``。
 
和支持类型的遗留命令系统不同,参数名和函数参数名不需要匹配。
唯一重要的方面是参数计数和参数顺序。
 
自定义命令在一个简单的包中提供了大量准将特性的子集,有意跳过一些不太常见的命令
很少使用的特性,如fork和redirects,几乎只在vanilla`execute`命令中使用。
 
### 命令参数类型
 
有几种默认参数类型可以直接使用,而无需指定自定义类型。每个参数都可以是
在app config的`arguments`部分自定义,通过需要的`type`指定其基类型
使用一系列可选修饰符匹配任何内置类型。共享修饰符包括：
*`suggest`-键入时在命令上方显示的建议的静态列表
*`suggester`-函数,使用一个map参数,指示已解析命令中属性的当前状态
建议输入有效建议的动态列表。例如,这里有一个基于术语的类型匹配
所有加载的玩家添加史蒂夫和亚历克斯,由于玩家列表随时间的变化不能静态提供：
  ```
__config() -> {
   'arguments' -> {
      'loadedplayer' -> {
         'type' -> 'term',
         'suggester' -> _(args) -> (
            nameset = {'Steve', 'Alex'}; 
            for(player('all'), nameset += _);
            keys(nameset)
         ),
      }
   }
};
  ```
*`区分大小写`-建议是否区分大小写,默认为true
  
下面是一个内置类型的列表,以及它们的返回值格式,以及一个修饰符列表
可以为该类型定制的(如果有)
*``string``：可以被引用以包含空格的字符串。可通过``options``-a自定义
它可以采用的有效选项的静态列表。如果键入的字符串不在此列表中,则命令将失败。
*``term`：单字字符串,没有空格。也可以使用`选项`进行自定义`
*``text``：命令的其余部分作为字符串。必须是最后一个论点。也可以使用`选项`进行自定义`
*`bool`：`真`或`假``
*浮点数：一个数字。可使用`min`和`max`值自定义。
*``int``：需要整数值的数字。可使用`min`和`max`值自定义。
*`偏航`：需要有效偏航角的数字。
*``pos`：以三重坐标表示的块位置。自定义为`loaded`,如果为true,则需要位置
待加载。
*``block`：包装在块值中的有效块状态(包括块属性和数据)
*``blockpredicate``：返回一个4元组,指示要匹配的块的条件：块名,块标记,
所需状态属性的映射,以及要匹配的标记。块名或块标记都为`null`,但不能同时为两者。
属性映射总是指定的,但它为空表示没有条件,并且匹配的nbt标记可以为`null`,表示
没有要求。从技术上讲,`all matching`谓词应该是`[null,null,{},null]`,但是
始终指定块名称或块标记。可以使用以下例程将块与此谓词匹配：
```
    block_to_match = block(x,y,z);
    [block_name, block_tag, properties, nbt_tag] = block_predicate;
   
    (block_name == null || block_name == block_to_match) &&
    (block_tag == null || block_tags(block_to_match, block_tag)) &&
    all(properties, block_state(block_to_match, _) == properties:_) &&
    (!tag || tag_matches(block_data(block_to_match), tag))
  ```
*``teamcolor``：团队的名称,以及16种有效团队颜色之一的整数值。
*``columnpos`：一对x和z坐标。
*``dimension`：表示世界上有效维度的字符串。
*`anchor`：一串`脚`或`眼`。
*``entitytype`：表示实体类型的字符串
*`floatrange`：两个数字的一对,其中一个比另一个小
*``players`：返回一个有效的播放器名称字符串列表,无论是否登录。如果配置为`single`,则只返回一个播放器或`null`。
*`intrange`：与`floatrange`相同,但需要整数。
*`附魔`：附魔的名称
*`slot`：提供库存类型和插槽的列表。可以用`restrict`配置
`玩家,装备,盔甲,武器,容器,村民,马限制了玩家的选择
可用插槽。Scarpet支持所有香草槽,除了`horse.cost`-箱子项目,而不是项目本身。这是你想要的
需要通过nbt直接管理自己。另外,对于改变其容量的实体,如骆驼,您需要检查自己是否
指定的容器槽对您的实体有效。
*``item``：项目类型,计数1和nbt的三倍。
*``message``：带有扩展嵌入播放器名称的文本
*``effect`：表示状态效果的字符串
*``path`：有效的nbt路径
*‘objective’：记分板目标名称的元组及其标准
*``criteria`：计分板标准的名称
*``particle``：粒子的名称
*``recipe`：有效配方的名称。可输入配方数据函数。
*``advancement``：进步的名称
*``lootcondition``：战利品条件
*``loottable`：一个loot表源的名称
*``attribute`：属性名
*`boss`：一个bossbar的名字
*`生物群落`：一个生物群落的名字
*`sound`：声音的名称
*``storekey``：当前有效数据存储密钥的字符串。
*``identifier``：任何有效的标识符minecraft:`前缀作为默认值被去除。
可使用提供有效标识符静态列表的`options`参数进行配置。
*``rotation``：表示偏航和俯仰值的两个数字对。
*``scoreholder`：有效分数持有者的字符串列表。可使用`single`自定义,使参数需要单个目标,如果缺少`null`,则重新调整它
*``scoreboardslot``表示记分板显示的有效位置的字符串。
*``swizzle``-一组轴作为字符串,已排序。
*``time``—表示持续时间的刻度数。
*``uuid``-有效uuid的字符串。
*``surfacelocation```-x和z坐标对,浮点数。
*``location``-x,y和z坐标的三倍,如果
提供整数坐标,可选修饰符为`true`。
 
## 维度警告
 
需要注意的是,对实体和块的大多数调用都会引用当前
调用者的维度,例如,如果我们使用`player(`all`)`函数列出所有参与者,
如果一个玩家在另一个维度中,对该玩家周围的实体和块的调用将是不正确的。
此外,在spawn块中运行commandblocks意味着命令将始终引用overworld
块和实体。如果您想在所有维度上运行命令,只需运行其中三个,
使用`/execute in overworld/the  nether/the  end run script run…`并使用`player(`*`)`查询播放器,
它只返回当前维度中的玩家,或者使用`in dimension(expr)`函数。
# 方块/世界API
 
## 指定块
 
### `block(x, y, z)`, `block(l(x,y,z))`, `block(state)`
 
返回指定位置的块或具有特定状态的块(由`/setblock`命令使用),
因此,允许块属性,块实体数据等,否则块可以通过其简单的
字符串名称,但仅在其默认状态下使用。
 
<pre>
block('air')  => air
block('iron_trapdoor[half=top]')  => iron_trapdoor
block(0,0,0) == block('bedrock')  => 1
block('hopper[facing=north]{Items:[{Slot:1b,id:"minecraft:slime_ball",Count:16b}]}') => hopper
</pre>
 
使用`block`函数检索块还有一个副作用,即评估其当前状态和数据。
因此,如果稍后使用它,它将反映块的状态和调用块时的块数据,而不是
当它被使用的时候。在`scan`函数等不同位置传递的块值除非
它的性质是必需的。这意味着,如果该位置的块在其在程序中被查询之前发生更改,则
可能导致获得稍后的状态,这可能不是所需的。考虑以下示例：
 
如果提供的输入无效,则抛出`unknown block`。
 
<pre>
set(10,10,10,'stone');
scan(10,10,10,0,0,0, b = _);
set(10,10,10,'air');
print(b); // 'air', block was remembered 'lazily', and evaluated by `print`, when it was already set to air
set(10,10,10,'stone');
scan(10,10,10,0,0,0, b = block(_));
set(10,10,10,'air');
print(b); // 'stone', block was evaluated 'eagerly' but call to `block`
</pre>
 
## 世界操纵
 
下面的所有函数都可以与block value一起使用,也可以用coord triple或3-long list查询。中的所有`pos`
下面引用的函数是指传递块位置的任一方法。
 
### `set(pos, block, property?, value?, ..., block_data?)`, `set(pos, block, [property?, value?, ...], block_data?)`, `set(pos, block, {property? -> value?, ...}, block_data?)`
 
`set`函数的第一个参数是坐标三元组,三个数字的列表或世界本地化块值。
第二个参数`block`是现有的块值,`block()`函数的结果,或者是指示块名的字符串值
具有可选的状态和块数据。之后是一个可选的
`额外块状态的属性-值对(也可以在列表或映射中提供)。可选的`block data`包括要删除的块数据
为目标块设置。
 
如果`block`仅由名称指定,则
目标块相同,跳过`set`操作,否则执行其他可能的额外操作
原始源块可能包含的属性。
 
返回的值要么是已设置的块状态,要么是`false`(如果跳过块设置或设置失败)
 
如果提供的要设置的块无效,则抛出`unknown block`
 
<pre>
set(0,5,0,'bedrock')  => bedrock
set(l(0,5,0), 'bedrock')  => bedrock
set(block(0,5,0), 'bedrock')  => bedrock
scan(0,5,0,0,0,0,set(_,'bedrock'))  => 1
set(pos(player()), 'bedrock')  => bedrock
set(0,0,0,'bedrock')  => 0   // or 1 in overworlds generated in 1.8 and before
scan(0,100,0,20,20,20,set(_,'glass'))
    // filling the area with glass
scan(0,100,0,20,20,20,set(_,block('glass')))
    // little bit faster due to internal caching of block state selectors
b = block('glass'); scan(0,100,0,20,20,20,set(_,b))
    // yet another option, skips all parsing
set(x,y,z,'iron_trapdoor')  // sets bottom iron trapdoor

set(x,y,z,'iron_trapdoor[half=top]')  // sets the top trapdoor
set(x,y,z,'iron_trapdoor','half','top') // also correct - top trapdoor
set(x,y,z,'iron_trapdoor', ['half','top']) // same
set(x,y,z,'iron_trapdoor', {'half' -> 'top'}) // same
set(x,y,z, block('iron_trapdoor[half=top]')) // also correct, block() provides extra parsing of block state

set(x,y,z,'hopper[facing=north]{Items:[{Slot:1b,id:"minecraft:slime_ball",Count:16b}]}') // extra block data
set(x,y,z,'hopper', {'facing' -> 'north'}, nbt('{Items:[{Slot:1b,id:"minecraft:slime_ball",Count:16b}]}') ) // same
</pre>
 
### `without_updates(expr)`
 
当块在世界中更改时,计算子表达式而不引起更新。
 
出于同步的考虑,以及受抑制的更新只能在一个滴答声内发生,
对`expr`的调用停靠在主服务器任务上。
 
考虑下面的场景：我们想在一个平坦的世界中生成一堆perlin噪声之后的地形
发电机。下面的代码会导致级联效应,因为放置在块边界上的块会导致其他块得到
满载,因此产生：
 
<pre>
__config() -> m(l('scope', 'global'));
__on_chunk_generated(x, z) -> (
  scan(x,0,z,0,0,0,15,15,15,
    if (perlin(_x/16, _y/8, _z/16) > _y/16,
      set(_, 'black_stained_glass');
    )
  )
)
</pre>
 
以下添加内容通过不允许块更新超过块边界来解决此问题：
 
<pre>
__config() -> m(l('scope', 'global'));
__on_chunk_generated(x, z) -> (
  without_updates(
    scan(x,0,z,0,0,0,15,15,15,
      if (perlin(_x/16, _y/8, _z/16) > _y/16,
        set(_, 'black_stained_glass');
      )
    )
  )
)
</pre>
 
### `place_item(item, pos, facing?, sneak?)`
 
在世界上使用一个给定的物品,就像它被玩家使用一样。物品名称是默认的minecraft物品名称,
减去minecraft前缀。默认面向是`向上`,但还有其他选项：`向下`,`北`,`东`,`南`,
`西`,但也有其他次要的方向,如楼梯,门等块放置重要。
尝试使用`北向上`这样的选项,该选项面朝北,光标指向屏幕的上部
block,或`up north`,意思是面朝上(玩家向下看)放置一个block,并将smidge放置在该block之外
中心向北。可选偷袭是一个布尔值,表示玩家在放置
块-此选项仅影响此时放置箱子和脚手架。
 
使用在放置的块上具有右键单击效果的项目,例如草上的`骨粉`或原木上的轴,
但不会打开箱子/容器,因此对交互块(如TNT,比较器等)没有影响。
 
如果placement/use为true,则返回true
成功,否则为假。
 
如果`item`不存在,则抛出`unknown item`
 
<pre>
place_item('stone',x,y,z) // places a stone block on x,y,z block
place_item('piston,x,y,z,'down') // places a piston facing down
place_item('carrot',x,y,z) // attempts to plant a carrot plant. Returns true if could place carrots at that position.
place_item('bone_meal',x,y,z) // attempts to bonemeal the ground.
place_item('wooden_axe',x,y,z) // attempts to strip the log.
</pre>
 
### `set_poi(pos, type, occupancy?)`
 
设置具有可选自定义占用的指定类型的兴趣点(POI)。默认情况下,不占用新的poi。
如果类型为`null`,则删除位置处的POI。在任何情况下,以前的POI也会被删除。可用的POI类型包括：
 
*`‘失业’,‘军械工’,‘屠夫’,‘制图工’,‘牧师’,‘农民’,‘渔夫’,‘弗莱彻’,‘皮革工’,‘图书管理员’,‘梅森’,‘傻瓜’,‘牧羊人’,‘工具匠’,‘武器匠’,‘家’,‘会议’,‘蜂巢’,‘蜂巢’,‘幽冥门’`
 
有趣的是,`失业者`和`傻瓜`在游戏中没有使用,也就是说,它们可以被用作永久性空间
scarpet应用程序的标记`会议室是唯一一个最大入住率增加了32人的会议室。
 
如果提供的兴趣点不存在,则抛出`unknown poi`
 
### `set_biome(pos, biome_name, update=true)`
 
改变了那个街区的生物群落。如果update被指定为false,那么块将不会被刷新
在客户身上。Biome的变化只能用区块中的全部数据发送给客户。
 
设定生物群落现在(从1.16开始)是特定维度的。最终改变了生物群落
仅当将其设置为y=0时才有效,并影响的整个列。在虚空中-你必须使用
你想要改变的生物群落的特定Y坐标,它影响大约4x4x4的区域(随机给定或取一些)
噪音)。
 
如果`biome name`不存在,则抛出`unknown biome`。
 
### `update(pos)`
 
导致位置处的块更新。
 
### `block_tick(pos)`
 
使块在位置处打勾。
 
### `random_tick(pos)`
 
在pos处运行随机刻
 
### `destroy(pos), destroy(pos, -1), destroy(pos, <N>), destroy(pos, tool, nbt?)`
 
像被玩家破坏一样摧毁方块。丝绸触感加-1,财富等级加正数。
如果指定了工具,并且可选地指定了它的nbt,它将使用该工具并尝试使用该工具挖掘块。
如果在没有项上下文的情况下调用,则与harvest不同,此函数将影响所有类型的块。如果与项目一起调用
在上下文中,它将无法打破一个生存玩家无法打破的积木。
 
如果没有item context,则如果未能销毁块,则返回`false`;如果块中断成功,则返回`true`。
在item上下文中,`true`表示中断item没有要使用的nbt,`null`表示工具应该
对于假设工具上的结果nbt标记,将其视为进程中断和`nbt`类型值。这取决于
程序员使用nbt将它应用到它所属的地方
 
如果`tool`不存在,则抛出`unknown item`。
 
下面是一个示例代码,可以使用播放器资源清册中的项来挖掘块,而不使用播放器上下文
用于采矿。显然,在这种情况下,使用`harvest`更为适用：
 
<pre>
mine(x,y,z) ->
(
  p = player();
  slot = p~'selected_slot';
  item_tuple = inventory_get(p, slot);
  if (!item_tuple, destroy(x,y,z,'air'); return()); // empty hand, just break with 'air'
  l(item, count, tag) = item_tuple;
  tag_back = destroy(x,y,z, item, tag);
  if (tag_back == false, // failed to break the item
    return(tag_back)
  );
  if (tag_back == true, // block broke, tool has no tag
    return(tag_back)
  );
  if (tag_back == null, //item broke
    delete(tag:'Damage');
    inventory_set(p, slot, count-1, item, tag);
    return(tag_back)
  );
  if (type(tag_back) == 'nbt', // item didn't break, here is the effective nbt
    inventory_set(p, slot, count, item, tag_back);
    return(tag_back)
  );
  print('How did we get here?');
)
</pre>
 
### `harvest(player, pos)`
 
使块由指定的播放器实体获取。荣誉玩家物品附魔,同时伤害
工具(如适用)。如果实体不是有效的播放器,则不会销毁任何块。如果一个球员不能突破
那个街区,一个街区也不会被摧毁。
 
### `create_explosion(pos, power?, mode?, fire?, source?, attacker?)`
 
在给定位置创建爆炸。可选参数的默认值为：``power`-`4`(TNT power),
``mode``(断块效果`none`,`destroy`或`break`：`break`,`fire`(是否应创建额外的防火块)-`false`,
`源`(分解实体)–`null`和`攻击者`(负责触发的实体)–`null`。用这个制造的爆炸
无法使用` on  explosion`事件捕获终结点,但它们将由` on  explosion  outcome`捕获。
 
### `weather()`,`weather(type)`,`weather(type, ticks)`
 
如果不带参数调用,则根据当前天气返回`clear`,`rain`或`thunder`。如果打雷,我会的
一定要返回`雷声`,否则会根据当前天气返回`雨`或`晴`。
 
使用一个参数(`clear`,`rain`或`thunder`),返回该天气类型的剩余滴答数。
注：没有雷雨也能打雷,必须有雨和雷才能形成暴风雨。
 
使用两个参数,将天气设置为`ticks`ticks的`type`。
 
## 方块和世界查询
 
### `pos(block), pos(entity)`
 
返回指定块或实体的三维坐标。从技术上讲,实体是用`query`函数查询的
同样可以通过`query(entity,`pos`)`实现,但为了简单起见,`pos`允许传递所有位置对象。
 
<pre>
pos(block(0,5,0)) => l(0,5,0)
pos(player()) => l(12.3, 45.6, 32.05)
pos(block('stone')) => Error: Cannot fetch position of an unrealized block
</pre>
 
### `pos_offset(pos, direction, amount?)`
 
返回在指定的`direction`上偏移`amount`块的坐标三元组。默认偏移量为
1个街区。若要偏移到对面,请对`amount`使用负数。
 
<pre>
pos_offset(block(0,5,0), 'up', 2)  => l(0,7,0)
pos_offset(l(0,5,0), 'up', -2 ) => l(0,3,0)
</pre>
 
### `(Deprecated) block_properties(pos)`
 
已被`keys(block state(pos))`代替
 
### `(Deprecated) property(pos, name)`
 
被`block state(pos,name)`代替
 
### `block_state(block)`, `block_state(block, property)`
 
如果仅与`block`参数一起使用,它将返回块属性及其值的映射。如果块没有属性,则返回
空地图。
 
如果指定了`property`,则返回该属性的字符串值;如果属性不适用,则返回`null`。
 
返回的值或属性总是字符串。期望用户知道期望和转换什么
使用`number()`函数将值转换为数字,或使用`bool()`函数将值转换为布尔值。返回的字符串值可以直接使用
在需要块特性的各种应用程序中返回状态定义。
 
`block_state`也可以接受块名作为输入,返回块的默认状态。
 
如果提供的输入无效,则抛出`unknown block`。
 
<pre>
set(x,y,z,'iron_trapdoor','half','top'); block_state(x,y,z)  => {waterlogged: false, half: top, open: false, ...}
set(x,y,z,'iron_trapdoor','half','top'); block_state(x,y,z,'half')  => top
block_state('iron_trapdoor','half')  => top
set(x,y,z,'air'); block_state(x,y,z,'half')  => null
block_state(block('iron_trapdoor[half=top]'),'half')  => top
block_state(block('iron_trapdoor[half=top]'),'powered')  => false
bool(block_state(block('iron_trapdoor[half=top]'),'powered'))  => 0
</pre>
 
### `block_list()`, `block_list(tag)`
 
返回所有块的列表。如果提供了标记,则返回属于此块标记的块的列表。
 
### `block_tags()`, `block_tags(block)`, `block_tags(block, tag)`
 
如果没有参数,则返回可用标记的列表,如果提供了块(通过坐标或块名),则返回lost
如果指定了一个标记,则如果标记无效,则返回`null`,如果此块不属于,则返回`false`
如果块属于该标记,则返回`true`。
 
如果`block`不存在,则抛出`unknown block`
 
### `block_data(pos)`
 
返回与特定位置关联的NBT字符串,如果块不包含块数据,则返回null。可以是当前
用于匹配其中的特定信息,或用于将其复制到另一个块
 
<pre>
block_data(x,y,z) => '{TransferCooldown:0,x:450,y:68, ... }'
</pre>
 
### `poi(pos), poi(pos, radius?, type?, status?, column_search?)`
 
在给定位置查询POI(兴趣点),如果找不到,则返回`null`,或POI类型的元组及其属性
占用负荷。使用可选的`type`,`radius`和`status`,返回一个区域中围绕`pos`的poi列表
给定`radius`。如果指定了`type`,则只返回该类型的poi类型,如果省略则返回所有类型或`any`。
如果指定了`status`(any`,`available`,`occumped`),则只返回具有该状态的poi。
当`column search`设置为`true`时,它将返回一个长方体中的所有poi,其中x和z上的`radius`块位于整个
阻止从0到255的列。Default(`false`)返回以`pos`为中心,半径为`radius`的球形区域内的poi`
半径。
 
`poi`调用的所有结果都按到请求的`pos`中心的欧几里德距离的排序顺序返回。
 
结果的返回格式是poi类型,占用负载和额外三倍坐标的列表。
 
使用radius查询POI是POI机制的预期用途,也是访问单个POI的能力
via`poi(pos)`in仅为完整性提供。
 
<pre>
poi(x,y,z) => null  // nothing set at position
poi(x,y,z) => ['meeting',3]  // its a bell-type meeting point occupied by 3 villagers
poi(x,y,z,5) => []  // nothing around
poi(x,y,z,5) => [['nether_portal',0,[7,8,9]],['nether_portal',0,[7,9,9]]] // two portal blocks in the range
</pre>
 
### `biome()` `biome(name)` `biome(block)` `biome(block/name, feature)`
 
如果没有参数输入,返回世界生物群落列表。
 
With block或name返回该位置生物群落的名称,如果提供的生物群落或块无效,则抛出`未知生物群落`。
 
通过一个可选的特性,它返回该生物群落的指定属性的值。可用和可查询的功能包括：
*``top material``：表示顶面材质的未定位块
*``under material``：未定位的块,表示位于表土下面的内容
*`类别`：这个生物群落起源的母体生物群落。可能的值包括：
`'none'`, `'taiga'`, `'extreme_hills'`, `'jungle'`, `'mesa'`, `'plains'`, `'savanna'`,
`'icy'`, `'the_end'`, `'beach'`, `'forest'`, `'ocean'`, `'desert'`, `'river'`,
`'swamp'`, `'mushroom'` 和  `'nether'`.
*``temperature``：从0到1的温度
*``fog color``：雾的RGBA颜色值
*``foolige color``：叶的RGBA颜色值
*``sky color`：天空的RGBA颜色值
*``water color`：水的RGBA颜色值
*``water fog color`：水雾的RGBA颜色值
*`湿度`：从0到1的值,表示生物群落的湿度
*‘降水量’：‘雨’‘雨’或‘无’。。。好吧,也许是`雪`,但那也意味着鼻涕。
*``depth``：浮点值,指示地形应产生的高低。值>0表示生成高于海平面
数值<0,低于海平面。
*``scale``：表示地形平坦程度的浮点值。
*`特征`：生物群落中生成的特征列表,按生成步骤分组
*`结构`：生物群落中产生的结构列表。
 
### `solid(pos)`
 
布尔函数,如果块是实心的,则为真。
 
### `air(pos)`
 
布尔函数,如果块是空的,则为真。。。或洞穴空气。。。或虚空的空气。。。或者其他空气。
 
### `liquid(pos)`
 
布尔函数,如果块是液体,则为真;如果块是浸水的(有任何液体),则为真。
 
### `flammable(pos)`
 
布尔函数,如果块易燃,则为真。
 
### `transparent(pos)`
 
布尔函数,如果块是透明的,则为真。
 
### `opacity(pos)`
 
数值函数,返回块的不透明度级别。
 
### `blocks_daylight(pos)`
 
布尔函数,如果块阻挡日光,则为真。
 
### `emitted_light(pos)`
 
数值函数,返回从块发射的灯光级别。
 
### `light(pos)`
 
数值函数,返回位置处的总光照级别。
 
### `block_light(pos)`
 
数值函数,返回位置处的块光(来自火把和其他光源)。
 
### `sky_light(pos)`
 
数值函数,返回位置处的天光(从天空访问)。
 
### `see_sky(pos)`
 
布尔函数,如果块可以看到天空,则返回true。
 
### `hardness(pos)`
 
数字函数,表示块的硬度。
 
### `blast_resistance(pos)`
 
数值函数,表示块的抗冲击强度。
 
### `in_slime_chunk(pos)`
 
布尔值,指示给定的块位置是否在史莱姆区块中。
 
### `top(type, pos)`
 
根据
`type`指定的heightmap。有效选项包括：
 
*`light`：最上面的遮光块(仅1.13)
*`motion`：最上面的运动块
*`terrain`：除叶子外最上面的运动阻挡块
*`洋底`：最顶层的非水区
*`surface`：最上面的曲面块
 
<pre>
top('motion', x, y, z)  => 63
top('ocean_floor', x, y, z)  => 41
</pre>
 
### `suffocates(pos)`
 
布尔函数,如果块导致窒息,则为真。
 
### `power(pos)`
 
数值函数,返回红石在位置的功率水平。
 
### `ticks_randomly(pos)`
 
布尔函数,如果块随机滴答,则为真。
 
### `blocks_movement(pos)`
 
布尔函数,如果位置处的块阻止移动,则为真。
 
### `block_sound(pos)`
 
返回块在位置处生成的声音类型的名称。什么之中的一个：
 
`'wood'`, `'gravel'`, `'grass'`, `'stone'`, `'metal'`, `'glass'`, `'wool'`, `'sand'`, `'snow'`, 
`'ladder'`, `'anvil'`, `'slime'`, `'sea_grass'`, `'coral'`
 
### `material(pos)`
 
返回位置处块的材质名称。针对一组块非常有用。什么之中的一个：
 
`'air'`, `'void'`, `'portal'`, `'carpet'`, `'plant'`, `'water_plant'`, `'vine'`, `'sea_grass'`, `'water'`, 
`'bubble_column'`, `'lava'`, `'snow_layer'`, `'fire'`, `'redstone_bits'`, `'cobweb'`, `'redstone_lamp'`, `'clay'`, 
`'dirt'`, `'grass'`, `'packed_ice'`, `'sand'`, `'sponge'`, `'wood'`, `'wool'`, `'tnt'`, `'leaves'`, `'glass'`, 
`'ice'`, `'cactus'`, `'stone'`, `'iron'`, `'snow'`, `'anvil'`, `'barrier'`, `'piston'`, `'coral'`, `'gourd'`, 
`'dragon_egg'`, `'cake'`
 
### `map_colour(pos)`
 
返回位置处块的地图颜色。什么之中的一个：
 
`'air'`, `'grass'`, `'sand'`, `'wool'`, `'tnt'`, `'ice'`, `'iron'`, `'foliage'`, `'snow'`, `'clay'`, `'dirt'`, 
`'stone'`, `'water'`, `'wood'`, `'quartz'`, `'adobe'`, `'magenta'`, `'light_blue'`, `'yellow'`, `'lime'`, `'pink'`, 
`'gray'`, `'light_gray'`, `'cyan'`, `'purple'`, `'blue'`, `'brown'`, `'green'`, `'red'`, `'black'`, `'gold
'`, `'diamond'`, `'lapis'`, `'emerald'`, `'obsidian'`, `'netherrack'`, `'white_terracotta'`, `'orange_terracotta'`, 
`'magenta_terracotta'`, `'light_blue_terracotta'`, `'yellow_terracotta'`, `'lime_terracotta'`, `'pink_terracotta'`, 
`'gray_terracotta'`, `'light_gray_terracotta'`, `'cyan_terracotta'`, `'purple_terracotta'`, `'blue_terracotta'`, 
`'brown_terracotta'`, `'green_terracotta'`, `'red_terracotta'`, `'black_terracotta'`
 
 
### `loaded(pos)`
 
布尔函数,如果游戏机制可以访问块,则为真。通常`scarpet`不检查是否工作
加载区域-游戏将自动加载丢失的块。我们认为这是一个优势。香草味`fill/clone`
命令只检查指定的角的负载。
 
要检查一个块是否真的加载了,我的意思是在内存中,使用`generation status(x)！=null`,因为仍然可以加载块
在可玩区域之外,只是不被任何游戏机制进程使用。
 
<pre>
loaded(pos(player()))  => 1
loaded(100000,100,1000000)  => 0
</pre>
 
### `(Deprecated) loaded_ep(pos)`
 
布尔函数,如果根据1.13.2加载块并进行实体处理,则为真
 
从scarpet 1.6开始就不推荐使用`loaded status(x)>0`,或者只使用`loaded(x)`
 
### `loaded_status(pos)`
 
根据新的1.14区块票证系统返回加载状态,0表示不可访问,1表示边界区块,2表示红石勾选,
实体勾选3
 
### `is_chunk_generated(pos)`, `is_chunk_generated(pos, force)`
 
如果区块的区域文件存在,则返回`true`,
`false`否则。如果optional force为`true`,它还将检查块在其区域文件中是否有非空条目
可用于评估区块是否被游戏触及。
 
`generation status(pos,false)`仅适用于当前加载的块,generation status(pos,true)`将创建
一个空的加载块,即使它不是必需的,所以`is chunk generated`可以作为一个有效的代理来确定
如果区块实际存在。
 
运行`is chunk generated`is对世界没有影响,但由于它是一个外部文件操作,所以它是
比其他生成和加载的检查要昂贵得多(除非加载了区域)。
 
### `generation_status(pos), generation_status(pos, true)`
 
根据票证系统返回生成状态。可以从几个可用但不可用的块中返回任何值
只能在几个状态下保持稳定：`full`,`features`,`liquid carvers`和`structure start`。返回`null`
如果块不在内存中,除非用可选的`true`调用。
 
### `inhabited_time(pos)`
 
返回块的时间。
 
### `spawn_potential(pos)`
 
返回某个位置的刷怪势能(仅限1.16+)
 
### `reload_chunk(pos)`
 
向客户端发送完整的区块数据。当发生了很多事情并且你想在客户机上更新它时很有用。
 
### `reset_chunk(pos)`,`reset_chunk(from_pos,to_pos)`,`reset_chunk(l(pos,…))`
一次移除并重置区块,指定区域中的所有区块或列表中的所有区块,移除之前的所有区块
块和实体,并用新一代取代它。对于所有当前加载的块,它们将被
到他们当前的世代状态,并更新到玩家。不在加载区域中的所有块将仅
生成到`结构启动`状态,允许玩家访问时完全生成。
游戏尚未触及的区域中的块将不会生成/重新生成。
 
它返回一个`map`,其中包含一个报告,指出有多少块受到影响,以及每个步骤所用的时间：
*`requested chunks`：请求区域或列表中的块的总数
*`受影响的块`：将被删除/重新生成的块数
*`loaded chunks`：请求区域/列表中当前加载的块数
*`relit count`：重新生成块的数目
*‘重新点燃时间’：重新点燃块所用的时间
*`layer count\<status>`：为其执行`<status>`生成步骤的块数
*`layer time\<status>`：用于生成`<status>`步骤的所有块的累计时间
 
### add_chunk_ticket(pos, type, radius)
 
在一个位置添加一个块票,这使得游戏保持指定区域的中心
`按`type`定义的预定义记号量加载半径为`radius`的pos。允许的类型
分别是`传送门`：300分,`传送门`：5分,`未知`：1分。半径可以是1到32个记号。
 
这个函数是暂时的-当chunk-ticket API被适当充实时,可能会发生变化。
 
## 结构和世界生成特性API
 
Scarpet提供了方便的方法来访问和修改有关结构的信息以及在游戏中生成
结构和特征。如果您正在使用scarpet,那么您可以使用的可用选项和名称的列表主要取决于您是否正在使用scarpet
对于minecraft 1.16.1及以下版本或1.16.2及以上版本,Mojang在1.16.2中添加了对worldgen特性的JSON支持
这意味着从1.16.2开始,它们就有了可以被数据包和scarpet使用的官方名称。如果你最近
在1.16.4版本中,您可以使用`plop()`来获取所有可用的worldgen功能,包括自定义功能和结构
由数据包控制。它返回以下类别的列表映射：
``scarpet custom``,`configured features`,`structures`,`features`,`configured structures``
 
### 以前的结构名称,包括变体(MC1.16.1及以下版本)
*   `'monument'`海洋纪念碑。在固定的Y坐标下生成,以水包围自身。
*   `'fortress'`幽冥要塞。海拔高度不同,但受代码限制。
*   `'mansion'`林地豪宅
*   `'jungle_temple'`丛林神殿
*   `'desert_temple'`大漠寺。在固定的Y高度生成。
*   `'endcity'`用Shulkers结束城市(在1.16.1中为`endcity`)
*   `'igloo'`冰屋
*   `'shipwreck'`海难
*   `'shipwreck2'`海难,搁浅
*   `'witch_hut'`
*   `'ocean_ruin'`, `ocean_ruin_small'`, `ocean_ruin_tall'`海洋废墟的石头变体。
*   `'ocean_ruin_warm'`, `ocean_ruin_warm_small'`, `ocean_ruin_warm_tall'`海洋遗迹的砂岩变体。
*   `'treasure'`一个宝箱。是的,它是一个整体结构。
*   `'pillager_outpost'`劫掠者前哨。
*   `'mineshaft'`矿井。
*   `'mineshaft_mesa'`矿井的台地(荒地)版本。
*   `'village'`平原,橡树村。
*   `'village_desert'`沙漠,砂岩村庄。
*   `'village_savanna'`大草原,相思村。
*   `'village_taiga'`taiga,云杉村。
*   `'village_snowy'`加拿大,雷诺尔。
*   `'nether_fossil'`一堆骨头(1.16)
*   `'ruined_portal'`毁灭之门,随机变量。
*   `'bastion_remnant'`Piglin堡垒,块的随机变体(1.16)
*   `'bastion_remnant_housing'`庇林堡垒的住房单元版本(1.16)
*   `'bastion_remnant_stable'`霍格林马厩版的q piglin bastion(1.16)
*   `'bastion_remnant_treasure'`庇林堡垒的宝藏室版本(1.16)
*   `'bastion_remnant_bridge'` 庇格林堡垒的桥版(1.16)

 
### 功能名称(1.16.1及以下版本)
*   `'oak'`
*   `'oak_beehive'`: 橡木蜂窝(1.15+)。
*   `'oak_large'`: 有树枝的橡树。
*   `'oak_large_beehive'`: 有树枝和蜂窝的橡树(1.15+)。
*   `'birch'`
*   `'birch_large'`: 桦树的高大变种。
*   `'shrub'`: 生长在丛林中的矮灌木。
*   `'shrub_acacia'`: 矮灌木,但配置有相思树(仅1.14)
*   `'shrub_snowy'`: 低矮的灌木丛,带白色方块(仅1.14)
*   `'jungle'`: 一棵树
*   `'jungle_large'`: 2x2丛林树
*   `'spruce'`
*   `'spruce_large'`: 2x2云杉树
*   `'pine'`: 具有最小叶龄的云杉(1.15+)
*   `'pine_large'`: 2x2云杉,最小叶龄(1.15+)
*   `'spruce_matchstick'`: 见1.15松木(仅1.14)。
*   `'spruce_matchstick_large'`: 见1.15松木大(仅1.14)。
*   `'dark_oak'`
*   `'acacia'`
*   `'oak_swamp'`: 有更多叶子和藤蔓的橡树。
*   `'well'`: 沙漠井
*   `'grass'`: 一些高高的草
*   `'grass_jungle'`: 小草丛特征(仅1.14)
*   `'lush_grass'`: 有斑驳蕨类植物的草(1.15+)
*   `'tall_grass'`: 2高草片(1.15+)
*   `'fern'`: 一些随机的2高蕨类植物
*   `'cactus'`: 随机的仙人掌
*   `'dead_bush'`: 几个随机的死布希
*   `'fossils'`: 地下化石,位置有点古怪
*   `'mushroom_brown'`: 大的棕色蘑菇。
*   `'mushroom_red'`: 大红蘑菇。
*   `'ice_spike'`: 冰钉。需要在下面放置雪垫。
*   `'glowstone'`: 辉石星团。上面需要尼日拉克。
*   `'melon'`: 一片甜瓜
*   `'melon_pile'`: 一堆瓜(1.15+)
*   `'pumpkin'`: 一片南瓜
*   `'pumpkin_pile'`: 一堆南瓜(1.15+)
*   `'sugarcane'`
*   `'lilypad'`
*   `'dungeon'`: 地牢。这些问题很难解决,而且经常失败。
*   `'iceberg'`: 冰山。在海平面上生成。
*   `'iceberg_blue'`: 蓝色的冰山。
*   `'lake'`
*   `'lava_lake'`
*   `'end_island'`
*   `'chorus'`: 合唱植物。需要放置尾石。
*   `'sea_grass'`: 一片海草。需要水。
*   `'sea_grass_river'`: 一个变种。
*   `'kelp'`
*   `'coral_tree'`, `'coral_mushroom'`, `'coral_claw'`: 珊瑚种类繁多,颜色随意。
*   `'coral'`: 随机的珊瑚结构。产卵需要水。
*   `'sea_pickle'`
*   `'boulder'`: 一个巨大的塔伊加生物群落中的岩石,苔藓形成。无法正确更新客户端,需要重新登录。


### 标准结构(自MC1.16.2+起)
 
使用`plop()：`structures``,但它总是返回以下内容：
 
``堡垒遗迹`,`埋藏的宝藏`,`沙漠金字塔`,`终点城`,`堡垒`,`冰屋`,
``丛林金字塔`,`大厦`,`矿井`,`纪念碑`,`地下化石`,`海洋废墟`,
``劫掠者前哨`,`废墟之门`,`海难`,`据点`,`沼泽小屋`,`村庄``
 
### 结构变体(自MC1.16.2+起)
 
使用`plop()：`configured structures``,但它总是返回以下内容：
 
``堡垒遗迹`,`埋藏的宝藏`,`沙漠金字塔`,`城市尽头`,`堡垒`,`冰屋`,
``丛林金字塔`,`大厦`,`矿井`,`矿井台地`,`纪念碑`,`地下化石`,
``海洋毁灭寒冷`,`海洋毁灭温暖`,`掠夺者前哨`,`毁灭之门`,`毁灭之门沙漠`,
``毁灭之门`丛林`,`毁灭之门`山脉`,`毁灭之门`海底`,`毁灭之门`海洋`,
``破败的门`沼泽`,`沉船`,`沉船搁浅`,`堡垒`,`沼泽小屋`,
``沙漠村`,`平原村`,`稀树草原村`,`斯诺维村`,`塔伊加村``
 
### 世界新一代功能(从MC1.16.2+开始)
 
使用`plop()：`features`和`plop()：`configured features`查看可用选项的列表。您的输出可能会因
数据包安装在您的世界中。
 
### 定制围巾功能
 
使用`plop()：`scarpet custom``查看完整列表。
 
这些包含一些常用的特性和结构,这些特性和结构不可能或很难通过普通的结构/特性获得。
 
*`堡垒残余桥`-堡垒残余桥版本
*`堡垒残余` hoglin stable`-霍格林马厩是堡垒残余的版本
*`堡垒残余之宝`-堡垒残余之宝版本
*`堡垒残余物单位`-堡垒残余物的住房单位版本
*`birch bees`-总是用蜂箱生成的桦树,不同于用概率生成的标准
*`珊瑚`-随机独立的珊瑚特征,通常是`温暖的海洋植被`的一部分`
*``coral claw``-爪珊瑚特征
*`珊瑚蘑菇`-蘑菇珊瑚特色
*`珊瑚树`-树珊瑚特征
*‘花式橡树蜜蜂’——一种大橡树变种,带有强制性蜂巢,不同于标准的概率产生
*`橡树蜜蜂`-普通的橡树,有一个管理蜂巢,不同于标准的概率产生
 
 
### `结构合格性(需要位置,结构,大小)`
 
检查给定块中结构的wordgen资格。需要`标准结构`名称(见上文)。
如果没有给出结构,或者`null`,那么它将检查
适用于所有结构。如果还请求了结构的边界框,它将计算势的大小
结构。与`structure*`类别中的其他函数不同,此函数既不使用世界数据,也不访问块
使其更适合于确定未生成地形的范围,但需要一些计算资源来计算结构。
  
与`structure`不同,这将返回一个暂定的结构位置。世界世代中的随机因素可能会阻止
形成的实际结构。
  
如果指定了structure,则如果块不合格或无效,它将返回`null`;如果结构应该出现,则返回`true`;或者
有两个值的地图：`box`表示一对坐标,表示结构的边界框,`pieces`表示
结构元素的列表(作为元组),包括其名称,方向和块的框坐标。
 
如果没有指定结构,它将返回一组符合条件的结构名称,或者一个带有结构的映射
以及与单个结构调用相同类型的映射值。空集或空映射表示
应该在那里生成。
 
如果结构不存在,则抛出`unknown structure`。
 
### `structures(pos),结构(pos,结构名称)`
 
返回给定块位置的结构信息。请注意,结构信息对于所有
来自同一块的块`structures`函数可以用块调用,也可以用块和结构名调用。在
第一种情况,它返回给定位置的结构映射,由结构名称键入,值指示
结构的边界框-一对三值坐标(参见示例)。使用额外结构调用时
name,返回一个带有两个值的映射,其中`box`表示结构的边界框,`pieces`表示结构的列表
该结构的组件,以及它们的名称,方向和两组坐标
表示结构件的边界框。如果结构无效,则其数据将为`null`。
 
需要`标准结构`名称(见上文)。
 
### `structure references(pos),结构 references(pos,结构 name)`
 
返回具有给定块位置的块所属的结构信息`结构和引用`function
可以用块调用,也可以用块和结构名调用。在第一种情况下,它返回一个结构名称列表
那是我的。当使用额外的结构名调用时,返回指向
保持结构的块中的最低块位置从这些结构开始。您可以查询该块结构
然后得到它的边界框。
 
需要`标准结构`名称(见上文)。
 
### `set structure(pos,structure name),set structure(pos,structure name,null)`
 
创建或删除与`pos`块关联的结构的结构信息。与`plop`不同,块是
不放置在世界中,只设置结构信息。对于游戏来说,这是一个功能齐全的结构
如果未设置块。要删除给定点所在的结构,请使用`structure references`查找当前位置
结构开始。
 
需要`Structure Variant`或`Standard Structure`名称(见上文)。如果使用标准名称,则
结构可能取决于生物群落,否则将生成该类型的默认结构。
 
如果结构不存在,则抛出`unknown structure`。
 
###‘砰的一声(pos,what)’`
 
在一个给定的`位置`上弹出一个结构或特征,因此块,三位置坐标或一系列坐标。
`什么`被扑通一声,确切地说,它通常取决于特征或结构本身。
 
需要`Structure Variant`,`Standard Structure`,`World Generation Feature`或`Custom Scarpet Feature`名称(请参见
上面)。如果使用标准名称,结构的变体可能取决于生物群落,否则为默认值
将生成此类型的结构。
 
所有结构都是块对齐的,并且通常跨越多个块。重复调用以扑通同一块中的结构
会导致相同的结构产生在彼此的顶部,或者具有不同的状态,但位置相同。
大多数结构在特定的高度上生成,这些高度是硬编码的,或者在它们周围有特定的块。API将
取消所有额外的位置/生物群落/结构/特征放置的随机要求,但有些是硬编码的
限制可能仍然会导致某些结构/功能无法放置。某些功能需要使用特殊的块
现在,像珊瑚->水或冰刺->雪块,和一些特征,如化石,安置是各种各样的
当然搞砸了。通过`set structure`设置结构信息,可以部分避免结构的这种情况,
它设置它而不看世界块,然后用`plop`填充块。这可能有效,也可能无效。
 
所有生成的结构都将保留它们的属性,比如mob产卵,但是在许多情况下,世界/维度
它本身就有一定的规则来繁殖怪物,就像在超世界中扑通一个幽冥堡垒不会繁殖幽冥怪物,
因为幽冥怪物只能在幽冥中产卵,但在幽冥中扑通一声-会表现得像一个有效的幽冥堡垒。
 
### (已弃用)`custom维度(名称,种子?)`
 
已被`create datapack()`弃用,后者可用于设置自定义维度
 
确保具有给定`name`的维度可用,并使用给定的种子进行配置。它只会改变世界
生成器设置,以及可选的自定义种子(如果未提供,则使用当前世界种子)。
 
如果具有此名称的维度已存在,则返回`false`,并且不执行任何操作。
 
使用`custom dimension`创建的维度只在游戏重新启动之前存在(与数据包相同,如果已删除),但是
所有的世界数据都应该保存。如果下次加载应用程序时重新创建自定义维度,它将使用
现有的世界内容。这意味着由程序员来确定自定义尺寸设置
存储在应用程序数据中,并在应用程序重新加载并希望使用以前的世界时还原。因为香草游戏
如果尚未通过`自定义维度`配置维度,则跟踪世界数据,而不是世界设置
应用程序还没有初始化他们的维度,玩家将被定位在同一个坐标的超世界。
 
自定义维度列表(用于`/execute in<dim>`)仅在加入
游戏,意味着玩家加入后创建的自定义世界将不会在香草命令中被建议,而是运行
对他们的普通命令将是成功的。这是因为总是加载带有维度的数据包
在游戏中,假设没有改变。
 
`自定义维度是实验性的,被认为是在制品。更多的自定义选项除了种子将被添加到
未来。
 
#在较大的块区域上迭代
 
这些函数有助于扫描较大区域的块,而无需使用泛型循环函数,如嵌套的`loop`。
 
### `scan(中心,范围,上限?,表达式)`
 
计算由其中心`center=(cx,cy,cz)`定义的块区域上的表达式,并在所有方向上展开
按` range=(dx,dy,dz)`块,或可选地用` range`坐标和` upper range`坐标表示负数
正值,所以如果您知道下坐标,并且通过调用``scan(center,0,0,0,w,h,d,…)`,可以使用它。
 
`center`可以定义为三个坐标,一个由三个坐标组成的元组或一个块值。
`range`和upper range`可以具有相同的表示形式,只要它们是块值,它就会计算到中心的距离
作为范围,而不是按原样获取值。
 
`expr`接收作为当前分析块和表示块本身的`````的坐标的``x,`y,`z`。
 
返回`expr`(布尔结果为`true`)的成功求值数,除非在void上下文中调用,
这将导致表达式的布尔值无法计算。
 
`scan还处理continue和break语句,使用continue的返回值代替表达式
返回值`break`返回值无效。
 
### `volume(从 pos,到 pos,expr)`
 
为区域中的每个块计算表达式,与`scan`函数相同,但使用
长方体。可以指定任何角点,就像使用`/fill`命令一样。
你可以用一个位置或三个坐标来指定,这无关紧要。
 
有关返回值和处理`break`和`continue`语句的信息,请参见上面的`scan`函数。
 
###邻居(pos)`
 
返回参数的6个相邻块的列表。通常与`for`等其他循环函数一起使用。
 
<pre>
对于(邻域(x,y,z),air())=>4//块周围的air块数
</pre>
 
### `rect(中心,范围?,上限?)`
 
返回一个迭代器,就像遍历块的矩形区域的`range`函数一样。如果只有中心
点被指定,它在27个块上迭代。如果指定了`range`参数,则按相应的
每个方向上的块数。如果指定了`positive range`参数,
它使用`range`表示负偏移量,`positive range`表示正偏移量。
 
``center`可以定义为三个坐标,三个坐标的列表或块值。
``range`和`positive range`可以具有相同的表示形式,只要它们是块值,它就会计算到中心的距离
作为范围,而不是按原样获取值`
 
### `菱形(中心位置,半径,高度?)`
 
在菱形块区域上迭代。没有半径和高度,它的7个街区以中间为中心
(街区+邻居)。指定半径后,它将在x和z坐标上展开形状,并在y坐标上以自定义高度展开形状。
其中任何一个也可以是零。半径为0表示棍子,高度为0表示菱形垫。
#实体API
 
##实体选择
 
在使用实体之前必须先获取它们。实体还可以在调用脚本之间更改其状态,如果
游戏滴答声出现在对程序的不同调用之间,或者如果程序自己调用`game tick`。
在这种情况下-需要重新获取实体,或者代码应该说明实体正在消亡。
 
### `player(),player(类型),player(名称)`
 
在没有参数的情况下,它返回调用方播放器或最接近调用方的播放器。
对于播放器范围的应用程序(这是一个默认值),它总是拥有的播放器或`null`如果它不存在,即使一些代码
仍然以他们的名义运行。
请注意,主上下文
将接收指向此播放机的`p`变量。如果指定了`type`或`name`,它将首先尝试匹配一个类型,
返回一个与类型匹配的玩家列表,如果失败,将假定其玩家名称查询使用
如果找不到玩家,则为`null`。有了`all`,游戏中所有玩家的列表,所有维度,所以结束
用户需要小心,你可能是指错误的块和实体周围的球员有问题。
使用`type=`*``返回调用者维度中的所有玩家,`survival``返回所有生存和冒险玩家,
``creative``返回所有有创意的玩家,`spectating``返回所有有创意的玩家,和``！看着````,
所有的人都不是在看球员。如果所有操作都失败,则使用`name`,如果相关玩家已登录。
 
### `entity id(uuid),entity id(id)`
 
通过`entity~`ID``获取实体的ID,这对于维度和当前世界是唯一的
运行,或通过UUID,通过`entity~ `UUID``获得。如果找不到这样的实体,则返回null。更安全的`存储`方式
调用之间的实体,因为缺少的实体将返回`null`。使用UUID或数字ID的两个调用都是`O(1)`,
但显然使用uuid需要更多的内存和计算。
 
### `实体列表(描述符)`
 
返回当前维度中与指定描述符匹配的实体的全局列表。
对`entity list`的调用总是从脚本执行的当前世界中获取实体。
 
### `实体类型(描述符)`
 
解析返回与其匹配的实体类型列表的给定描述符。返回的类型列表也是有效列表
可以在需要实体类型的其他地方使用的描述符。
 
目前,可以使用以下描述符：
 
*`*`：所有实体,甚至`！有效的`,匹配所有实体类型。
*`valid`-所有未死亡的实体(运行状况>0)。以下所有主要类别也仅限退货
`valid`类别中的实体。匹配所有实体类型。`！valid`匹配所有类型中已经失效的所有实体。
*‘有生命的’——所有类似某种生物的实体
*`投射物`-所有不存在的实体或类型,可以投射或投射,`！匹配所有类型
不是活着,而是不能抛掷或投射。
*`地雷车`匹配所有地雷车类型。`！雷车`匹配所有不是活动的类型,但也不是雷车。使用复数
因为`minecart`本身就是一个合适的实体类型。
*‘不死生物’,‘节肢动物’,‘水生生物’,‘普通生物’,‘农夫’——属于这些群体的所有实体/类型。全部
有生命的实体只属于其中一个。相应的负数(例如,`！亡灵`)对应于所有
但不属于那个群体。实体组用于交互/战斗机制中,比如对不死生物的打击,或者刺穿
对于水生生物。还有一些机械师与群体互动,比如和骗子一起敲钟。所有其他没有这些特征的暴徒都属于
到`普通`组。
*‘怪物’,‘生物’,‘环境物’,‘水’
以它们的繁殖群为基础的生物实体。否定描述符解析为所有不属于它的生命类型
类别。
*所有实体标签,包括与数据包一起提供的标签。内置实体标签包括：`skeletons`,`raiders`,
`蜂巢居住者(bee,duh),`箭头`和`撞击物`。
*任何标准实体类型,相当于从`/courm`命令中选择,这是返回的选项之一
按`entity types()`,但`fishing bobber`和`player`除外。
 
所有类别前面都可以加`！`它将获取所有有效(运行状况>0)但不可用的实体(除非另有说明)
属于那个团体。
 
### `entity\区域(类型,中心,距离)`
 
 
返回以`center`为中心且最多与`distance`块相距的区域中指定类型的实体
中心点/区域。使用与`实体列表`相同的`类型`选择器。
 
``center`和`distance`可以是坐标的三倍,也可以是`entity area`的三个连续参数`居中`can
也可以表示为一个块,在这种情况下,搜索框将集中在块的中间,或者一个实体-在这种情况下
实体的整个边界框用作搜索的`中心`,然后用`距离`向量向各个方向展开。
 
在任何情况下-返回边界框与由`center`和`distance`定义的边界框冲突的所有实体。
 
实体区域比`实体选择器`简单,运行速度快约20%,但仅限于预定义的选择器和
长方体搜索区域。
 
### `entity\选择器(选择器)`
 
返回满足给定实体选择器的实体。在所有选择实体的方法中最复杂的,
但是最能干的。选择器是缓存的,所以它应该和其他选择实体的方法一样快。不同于其他
实体获取/过滤方法,此方法不保证从当前维度返回实体,因为
选择器可以返回世界上任何加载的实体。
 
### `spawn(名称,位置,nbt?)`
 
在世界中生成并放置一个实体,如`/courm`命令。需要一个位置来繁殖,并且是可选的
要与实体合并的额外nbt数据。它与调用`run(`召唤…`)`不同的是
将实体作为返回值返回,这是swell。
 
## 实体操纵
 
与使用大量不同查询函数的块不同,实体是使用`query`进行查询的
函数并通过`modify`函数进行更改。所需的信息类型或要修改的值对于
每次通话。
 
`~`(in)运算符是`query`的别名。如果一个语句没有参数,
在这种情况下,可以从根本上简化：
 
<pre>
query(p,`name`)<=>p~`name`//更简洁
query(p,`holds`,`offhand`)<=>p~l(`holds`,`offhand`)//不是真的,但是可以完成
</pre>
 
### `query(e,`removed`)`
 
布尔型。如果删除实体,则为True。
 
### `query(e,`id`)`
 
返回实体的数字id。跟踪脚本中实体的最有效方法。
ID仅在当前游戏会话中是唯一的(在重新启动之间不会保留ID),
和维度(每个维度都有自己的ID,可以重叠)。
 
### `query(e,`uuid`)`
 
返回实体的UUID(唯一id)。可以使用其他普通命令和
无论维度如何,都保持唯一,并在游戏重新启动之间保留。显然,玩家不能
通过UUID访问,但应改为使用其名称访问。
 
<pre>
map(entities area(`*`,x,y,z,30,30,30),run(`kill`+query(,`id`))//不杀死玩家
</pre>
 
### `query(e,`pos`)`
 
实体头寸的三倍
 
### `查询(e,`位置`)`
 
实体位置(x,y和z坐标)和旋转(偏航,俯仰)的五元组
 
### `query(e,`x`),query(e,`y`),query(e,`z`)`
 
实体坐标的各个分量
 
### `query(e,`pitch`)`,`query(e,`yaw`)`
 
俯仰和偏航或实体正在查看的位置。
 
### `query(e,`head yaw`)`,`query(e,`body yaw`)`
 
适用于生活实体。设置各自的头部和身体朝向角度。
 
### `query(e,`look`)`
 
返回实体正在查看的三维矢量。
 
### `查询(e,`运动`)`
 
实体运动矢量的三倍,`l(运动x,运动y,运动z)`。运动代表所有力的速度
施加在给定实体上的。不是`力量`的东西,比如自发的运动,或者来自地面的反应,都是
不是上述力量的一部分。
 
### `query(e,`motion_x`),query(e,`motion_y`),query(e,`motion_z`)`
 
实体运动矢量的各个分量
 
### `查询(e,`在地面`)`
 
如果en实体站在坚实的地面上并因此而倒塌,则返回`true`。
 
### `query(e,`name`),query(e,`display name`),query(e,`custom name`),query(e,`type`)`
 
`display name`情况下的实体名称字符串或格式化文本`
 
<pre>
query(e,`name`)=>皮革工人
查询(e,`custom name`)=>空
query(e,`type`)=>村民
</pre>
 
### `query(e,`命令名称`)`
 
返回一个有效字符串,该字符串将用于对实体进行寻址的命令中。所有实体的UUID,除了
玩家,他们的名字在哪里。
 
<pre>
运行(`/kill`+e~`command_name`);
</pre>
 
### `查询(e,`持久性`)`
 
返回mob是否有持久性标记。对于非mob实体返回`null`。
 
### `query(e,`is riding`)`
 
布尔值,如果实体骑在另一个实体上,则为真。
 
### `查询(e,`is riden`)`
 
布尔值,如果另一个实体骑着它,则为真。
 
### `查询(e,`乘客`)`
 
实体列表。
 
### `query(e,`mount`)`
 
`e`乘坐的实体。
 
### `query(e,`scoreboard tags`)`,`query(e,`tags`)`(已弃用)
 
实体计分板标签列表。
 
### `query(e,`has scoreboard tag`,tag)`,`query(e,`has tag`,tag)`(已弃用)
 
布尔值,如果实体被标记为`tag`scoreboad标记,则为true。
 
### `query(e,`entity tags`)`
 
分配给此实体表示的类型的实体标记的列表。
 
### `query(e,`has entity tag`,标记)`
 
如果实体与该实体标记匹配,则返回`true`,如果不匹配,则返回`false`,如果标记无效,则返回`null`。
 
### `query(e,`is burning`)`
 
布尔值,如果实体正在燃烧,则为true。
 
### `query(e,`fire`)`
 
着火的剩余滴答数。
 
### `query(e,`silent`)`
 
布尔值,如果实体是静默的,则为true。
 
### `查询(e,`重力`)`
 
布尔值,如果实体像大多数实体一样受重力影响,则为真。
 
### `query(e,`invulnerable`)`
 
布尔值,如果实体不受攻击,则为真。
 
### `query(e,`immune to  fire`)`
 
布尔值,如果实体对激发免疫,则为真。
 
### `查询(e,`维度`)`
 
实体所在维度的名称。
 
### `query(e,`height`)`
 
以块为单位的实体高度。
 
### `查询(e,`宽度`)`
 
以块为单位的图元宽度。
 
### `query(e,`眼睛高度`)`
 
以块为单位的实体的眼睛高度。
 
### `查询(e,`年龄`)`
 
实体的年龄(单位：ticks),即它存在的时间。
 
### `查询(e,`繁殖年龄`)`
 
被动实体的繁殖年龄,以蜱为单位。如果是阴性,则是成年期,如果是阳性,则是繁殖冷却期。
 
### `query(e,`despawn timer`)`
 
对于活着的实体,它们落在玩家眼前之外的滴答声的数量。
 
### `query(e,`portal cooldown`)`
 
实体可以再次使用入口之前剩余的刻度数。
 
### `query(e,`portal timer`)`
 
实体在入口中的刻度数。
 
### `query(e,`item`)`
 
如果是项实体,则为项三元组(名称,计数,nbt),否则为`null`。
 
### `query(e,`count`)`
 
来自项实体的堆栈中的项数,否则为`null`。
 
### `query(e,`pickup delay`)`
 
检索项实体的拾取延迟超时,否则为`null`。
 
### `query(e,`is baby`)`
 
布尔值,如果是婴儿的话是真的。
 
### `query(e,`target`)`
 
返回mob的攻击目标,如果没有或不适用则返回null。
 
### `query(e,`home`)`
 
返回生物的原位(根据怪物的AI,皮带等),如果没有或不适用则返回空值。
 
### `query(e,`spawn point`)`
 
返回位置元组,维度,繁殖角度以及是否强制繁殖,假设玩家有繁殖位置。
如果未设置生成位置,则返回`false`;如果`e`不是玩家,则返回`null`。
 
### `query(e,`path`)`
 
返回实体的路径(如果存在),否则返回`null`。路径由节点列表组成,每个节点都是一个列表
包含块值,节点类型,惩罚和布尔值,指示是否已访问该节点。
 
### `query(e,`pose`)`
 
返回实体的姿势,以下选项之一：
*`站着``
*`秋天飞``
*`睡觉``
*`游泳``
*`旋转攻击``
*`蹲着``
*`快死了``
 
### `query(e,`潜行`)`
 
布尔值,如果实体是隐藏的,则为真。
 
### `query(e,`sprint`)`
 
Boolean,如果实体正在冲刺,则为true。
 
### `查询(e,`游泳`)`
 
布尔值,如果实体正在游动,则为真。
 
### `query(e,`跳跃`)`
 
布尔值,如果实体正在跳转,则为真。
 
### `query(e,`swing`)`
 
如果实体正在主动摆动其手,则返回`true`;如果不是,则返回`false`;如果摆动不适用,则返回`null`
那个实体。
 
### `query(e,`gamemode`)`
 
带有gamemode的字符串,如果不是玩家,则为`null`。
 
### `query(e,`gamemode id`)`
 
好的游戏模式id,如果不是玩家则为空。
 
### `query(e,`player type`)`
 
如果参数不是播放器,则返回`null`,否则：
 
*`singleplayer`：用于单人游戏
*`多人游戏`：专用服务器上的玩家
*`lan host`：为向lan打开游戏的单人所有者
*`lan player`：用于连接到局域网主机的所有其他播放器
*`假`：任何一个被打了屁股的假球员
*‘影子’：任何地毯阴影的真实玩家
*`领域`：?
 
### `查询(e,`类别`)`
返回包含实体类别(敌对,被动,水,环境,杂项)的小写字符串。
 
### `query(e,`团队`)`
 
实体的团队名称,如果未分配团队,则为`null`。
 
### `query(e,`ping`)`
    
播放机的ping以毫秒为单位,如果不是播放机,则为`null`。
 
### `query(e,`权限级别`)`
 
玩家的权限级别,如果不适用于此实体,则为`null`。
 
### `query(e,`客户品牌`)`
 
返回所连接客户端的可识别客户端类型。可能的结果包括`香草`或`地毯<version>`,其中
version表示连接的客户端的版本。
 
### `查询(e,`效果`,名称?)`
 
如果没有额外的参数,它将返回活动实体上的效果列表。每个条目都是短的三倍
效果名称,放大器和剩余持续时间(单位：ticks)。有一个论点,如果活着的实体没有激活的药剂,
返回`null`,否则返回放大器和剩余持续时间的元组。
 
<pre>
query(p,`effect`)=>[[haste,0,177],[speed,0,177]]
查询(p,`effect`,`haste`)=>[0177]
查询(p,`effect`,`resistance`)=>空
</pre>
 
### `query(e,`health`)`
 
表示剩余实体运行状况的数字,如果不适用,则为`null`。
 
### `查询(e,`饥饿`)`
### `query(e,`saturation`)`
### `查询(e,`用尽`)`
 
检索与玩家饥饿相关的信息。对于非玩家,返回`null`。
 
### `查询(e,`吸收`)`
 
获得玩家的全神贯注(黄色的心,例如当有一个金苹果。)
 
### `query(e,`xp`)`
### `query(e,`xp level`)`
### `query(e,`xp progress`)`
### `query(e,`score`)`
 
与玩家经验值相关的数字`xp`是玩家拥有的全部xp,`xp level`是在热工具栏中看到的级别,
`xp progress`是一个介于0和1之间的浮点数,表示xp条的填充百分比,`score`是死亡时显示的数字
 
### `query(e,`air`)`
 
表示剩余实体运行状况的数字,如果不适用,则为`null`。
 
### `查询(e,`语言`)`
 
对于任何非播放器实体返回`null`,如果不是,则以字符串形式返回播放器的语言。
 
### `query(e,`holds`,slot?)`
 
返回`slot`中项目的短名称,堆栈计数和NBT的三倍值,如果没有或不适用,则返回`null`。`slot`的可用选项有：
 
*`主手`
*`即兴`
*头`
*`胸部`
*`腿`
*`英尺`
 
如果未指定`slot`,则默认为主指针。
 
### `查询(e,`选定的插槽`)`
 
表示实体库存的选定插槽的数字。目前只适用于玩家。
 
### `query(e,`活动块`)`
 
返回玩家当前挖掘的区块,由游戏服务器注册。
 
### `query(e,`breaking progress`)`
 
返回当前玩家挖掘块的当前中断进度,如果没有挖掘块,则返回`null`。
中断进程(如果不为null)是0或以上的任意数字,而10表示该块应该已经被中断
被客户破坏了。如果客户端/连接滞后,则此值可能高于10。
 
例子：
 
下面的程序提供了自定义的断块时间,包括漂亮的断块动画,包括instamine
否则开采时间会更长的区块。
 
[视频演示](https://youtu.be/zvEEuGxgCio)
```py公司
全局块={
`橡木板`->0,
`黑曜石`->1,
`结束帧`->5,
`基岩`->10
};
  
__在玩家上单击阻止(玩家,阻止,面)->
(
步骤=全局_blocks:str(块);
if (步长==0,
销毁(块,-1);//instamine公司
,步骤！=无效的,
时间表(0,``u break`,player,pos(block),str(block),step,0);
)
);
 
_休息(玩家,位置,姓名,步幅,等级)->
(
当前=播放器~`活动块`;
if (当前！=名称| |位置(当前)！=销售时点情报系统,
修改(player,`breaking progress`,null);
   ,
修改(玩家,`突破进度`,等级);
if (等级>=10,销毁(位置,-1));
时间表(步骤,``u break`,玩家,位置,姓名,步骤,等级+1)
);
)
```
 
### `query(e,`facing`,order?)`
 
返回实体所面对的位置。可选顺序(数字从0到5,负数),表示主要方向
实体正在查看的位置。从最显著(0阶)到相反(5阶或-1阶)。
 
### `query(e,`trace`,reach?,options?…)`
 
从实体透视图返回光线跟踪的结果,指示光线跟踪的对象。默认范围为4.5
方块(5为有创造力的玩家),默认情况下,它跟踪方块和实体,与玩家攻击跟踪相同
行动。这可以通过`options`自定义,使用`blocks`跟踪块,使用`liquids`包括液体块
作为可能的结果,并用`实体`来跟踪实体。还可以指定`exact`,返回实际命中率
坐标为三元组,而不是块或实体值。以上的任何组合都是可能的。追踪时
实体和块,块将接管优先权,即使透明或非冲突
(又名在高草中斗鸡)。
 
无论选择什么选项,结果都可能是：
-如果没有任何东西可以触及,则返回null
-如果look以实体为目标,则为实体
-块值(如果块在可达范围内),或
-如果使用了`exact`选项并且命中成功,则为坐标三元组。
 
### `query(e,`属性`)``query(e,`属性`,名称)`
 
返回活动实体的属性值。如果没有提供名称,
返回此实体的所有属性和值的映射。如果属性不适用于实体,
或者实体不是活动实体,则返回`null`。
 
### `查询(e,`大脑`,记忆)`
 
检索实体的大脑记忆。可能的内存单元高度依赖于游戏版本。大脑可用
适用于村民(1.15+)和猪,猪,走狗和猪畜生(1.16+)。如果内存不存在或
对于实体不可用,返回`null`。
 
返回值的类型(实体,位置,数字,事物列表等)取决于请求的类型
记忆。除此之外,自1.16以来,内存可能会过期—在本例中,该值作为任何内容的列表返回
在那里,和当前的ttl在滴答声。
 
适用于1.15.2的可检索内存：
*‘家’,‘工地’,‘集合点’,‘二次工地’,‘暴徒’,‘看得见的暴徒’,‘看得见的村民’,
`最近的玩家`,`最近的可见玩家`,`行走目标`,`看目标`,`互动目标`,
`繁殖目标`,`path`,`interactiable门`,`opened门`,`nearest床`,`hurt by`,`hurt by entity`,
`最近的敌方`,`藏身处`,`听到钟声`,`无法到达`步行`目标`,
`傀儡最后一次看见时间,`最后一次睡觉`,`最后一次醒来`,`最后一次在波依工作``
 
从1.16.2起可用的可检索存储器：
*`home`,`job site`,`potential job site`,`meeting point`,`secondary job site`,`mobs`,`visible mobs`,
`可见村民,最近玩家,最近可见玩家,最近可见目标玩家,
`行走目标`,`look目标`,`attack目标`,`attack冷却目标`,`interaction目标`,`bride目标`,
`骑行目标`,`path`,`INTERACTIVE门`,`OPEN门`,`NEASTER床`,`HART by`,`HART by实体`,`AVIOD目标`,
`最近的敌方`,`藏身处`,`听到铃声`,`无法到达目标`,`最近发现傀儡`,
``最后一次睡觉`,`最后一次醒来`,`最后一次工作地点`,`最近的成人`,`最近的被通缉物品`,
`最近的敌人,`愤怒`,`普遍的愤怒`,`崇拜物品`,`时间试图达到崇拜物品`,
`禁用步行欣赏物品,禁用欣赏,最近狩猎,庆祝地点,跳舞,
`最近的可猎取者,最近的可猎取者,最近的可猎取者,最近的可猎取者,最近的不戴黄金者,
`附近的成年猪`,`最近的可见的成年猪`,`最近的可见的成年猪`,
`最近的可见的成人猪笼草`,`最近的可见的僵尸`,`可见的成人猪笼草计数`,
`可见的``成人``霍格林``计数`,`最近的`玩家`,拿着``通缉物品`,`最近吃了`,`最近的`,`驱虫剂`,`平静了``
 
 
### `查询(e,`nbt`,路径?)`
 
返回实体的完整NBT。如果指定了path,那么它只获取NBT中与
路径。有关`path`属性的说明,请参阅vanilla`/data get entity`命令。
 
请注意,与Minecraft API中的其他调用相比,对`nbt`的调用要昂贵得多,而且只应
在没有其他选项时使用。返回值的类型为`nbt`,可以使用nbt进行进一步操作
通过`get,put,has,delete`键入对象,因此首先尝试使用API调用。
 
##实体修改
 
与实体查询一样,实体修改通过一个函数进行。大多数位置和动作修改
目前不要在玩家身上工作,因为他们的位置是由客户控制的。
 
目前无法直接修改NBT,但可以始终使用`run(`data modify entity…`)`。
 
### `modify(e,`remove`)`
 
从游戏中移除(而不是杀死)实体。
 
### `modify(e,`kill`)`
 
杀死实体。
 
### `修改(e,`pos`,x,y,z),修改(e,`pos`,l(x,y,z))`
 
将实体移动到指定的坐标。
 
### `modify(e,`位置`,x,y,z,偏航,俯仰),modify(e,`位置`,l(x,y,z,偏航,俯仰))`
 
一次更改全部位置向量。
 
### `修改(e,`x`,x),修改(e,`y`,y),修改(e,`z`,z)`
 
在指定方向上更改实体的位置。
 
### `modify(e,`俯仰`,角度),modify(e,`偏航`,角度)`
 
更改实体的俯仰角或偏航角。
 
### `modify(e,`头部偏航`,角度)`,`modify(e,`身体偏航`,角度)`
 
对于生命实体,控制其头部和身体的偏航角。
 
### `修改(e,`移动`,x,y,z),修改(e,`移动`,l(x,y,z))`
 
将实体从其当前位置移动一个向量。
 
### `修改(e,`运动`,x,y,z),修改(e,`运动`,l(x,y,z))`
 
设置运动矢量(实体移动的位置和移动量)。
 
### `modify(e,`motion_z`,x),modify(e,`motion_y`,y),modify(e,`motion_z`,z)`
 
设置运动向量的相应分量。
 
### `修改(e,`加速`,x,y,z),修改(e,`加速`,l(x,y,z))`
 
将向量添加到运动向量。对实体施加力的最现实的方法。
 
### `modify(e,`自定义名称`)`,`modify(e,`自定义名称`,名称)`,`modify(e,`自定义名称`,名称,可见)`
 
设置实体的自定义名称。不带参数-清除当前自定义名称。可选可见影响
如果自定义名称始终可见,即使通过块也是如此。
 
### `modify(e,`persistence`,bool?)`
 
将实体持久性标记设置为`true`(默认)或`false`。只影响暴徒。顽固的暴徒
别灰心丧气,别指望暴徒。
 
### `modify(e,`年龄`,数字)`
 
修改实体的内部年龄计数器。摆弄它将直接影响复杂系统的人工智能行为
实体,所以谨慎使用。
 
### `modify(e,`拾取延迟`,数字)`
 
设置物料实体的拾取延迟。
 
### `modify(e,`繁殖年龄`,数字)`
 
设定动物的繁殖年龄。
 
### `modify(e,`despawn timer`,数字)`
 
设置一个自定义的消散计时器值。
 
### `modify(e,`portal cooldown`,编号)`
 
设置实体可以再次使用入口之前的自定义剩余刻度数。
 
### `modify(e,`portal timer`,编号)`
 
设置实体位于门户中的自定义记号数。
 
### `modify(e,`dismount`)`
 
卸下骑乘实体。
 
### `modify(e,`mount`,其他)`
 
将实体装入`other`。
 
### `modify(e,`drop passengers`)`
 
甩掉所有乘客。
 
### `modify(e,`mount\ U passengers`,passenger,?…),修改(e,‘乘客座’,l(乘客座))`
 
装载到`e`上所有列出的实体上。
 
### `modify(e,`tag`,tag,?…),修改(e,`标签`,l(标签))`
 
向实体添加标记。
 
### `modify(e,`清除标记`,标记,…),修改(e,`清除标记`,l(标记))`
 
从实体中删除标记。
 
### `modify(e,`talk`)`
 
制造噪音。
 
### `modify(e,`ai`,布尔值)`
 
如果用`false`值调用,它将禁用mob中的AI`true`将再次启用它。
 
### `modify(e,`no clip`,布尔值)`
 
设置实体是否服从任何碰撞,包括与地形和基本物理的碰撞。不影响
玩家,因为他们是由客户端控制的。
 
### `modify(e,`effect`,name?,duration?,amplifier?,show  particles?,show  icon?,ambient?)`
 
将状态效果应用于活动实体。接受多个可选参数,默认为 `0` ,`true`,
``真`和`假`。如果未指定持续时间,或者它为null或0,则效果将被移除。如果未指定名称,
它清除了所有的影响。
 
### `modify(e,`home`,null),modify(e,`home`,block,distance?),modify(e,`home`,x,y,z,distance?)`
 
将AI设置为停留在原点附近,距离原点不超过`distance`个街区`距离`默认为16个块。
`null`删除它_可能无法完全与内置AI的暴徒合作,比如村民。
 
 
### `modify(e,`spawn point`)`,`modify(e,`spawn point`,null)`,`modify(e,`spawn point`,pos,dimension?,angle?,forced?)`
 
将玩家重生位置更改为给定位置,可选尺寸(默认为当前玩家尺寸),角度(默认为
当前玩家面临)和强制/固定繁殖(默认为`假`)。如果未传递`none`或`nothing`,则表示重生点
将被重置(删除)。
  
### `modify(e,`gamemode`,gamemode?),modify(e,`gamemode`,gamemode id?)`
 
将玩家的游戏模式修改为您输入的任何字符串(不区分大小写)或数字。
 
*0:生存
*1:创意
*2:冒险
*3:观众
 
### `modify(e,`jumping`,布尔值)`
 
如果设置为true,将使实体不断跳转;如果设置为false,将阻止实体跳转。
请注意,跳跃参数可以完全由实体AI控制,因此不要期望这会导致
永久的影响。使用`jump`可以确保实体跳转一次。
 
需要一个活生生的实体作为参数。
 
### `modify(e,`jump`)`
 
将使实体跳转一次。
 
### `modify(e,`swing`)``modify(e,`swing`,`offhand`)`
 
使生命体摆动它们所需的肢体。
 
### `modify(e,`silent`,布尔值)`
 
使实体沉默或不沉默。
 
### `modify(e,`重力`,布尔值)`
 
切换实体的重力。
 
### `modify(e,`invulnerable`,布尔值)`
 
切换实体的抗毁性。
 
### `modify(e,`fire`,记号)`
 
将为`ticks`ticks点燃实体。设置为0熄灭。
 
### `修改(e,`饥饿`,值)`
### `modify(e,`饱和度`,值)`
### `modify(e,`耗尽`,值)`
 
直接修改玩家的原始组件。对非玩家没有影响
 
### `修改(e,`吸收`,值)`
 
设置播放器的吸收值。每一点都是半颗黄色的心。
 
### `modify(e,`add xp`,值)`
### `modify(e,`xp level`,值)`
### `modify(e,`xp progress`,值)`
### `modify(e,`xp score`,value)`
 
操纵播放器xp值-``add xp``您可能想要使用的方法
操作一个动作应该给出多少`xp`xp score``只影响你死后看到的数字,并且
``xp progress``控制热工具栏上方的xp progressbar,应取0到1之间的值,但您可以将其设置为任何值,
也许你会得到一个替身,谁知道呢。
 
### `modify(e,`air`,记号)`
 
修改实体空气
 
### `modify(e,`add`,值)`
 
将耗尽值添加到当前玩家耗尽级别-这可能是您想要使用的方法
操纵一次行动的`食物`成本。
 
### `modify(e,`breaking progress`,value)`
 
修改玩家当前布雷区块的破发进度。值为null时,`-1`使其复位。
值 `0` 到`10`将分别显示断块的动画。勾选`query(e,`breaking progress`)`for
举例说明。
 
### `modify(e,`nbt merge`,部分标记)`
 
将部分标记合并到实体数据中,并从更新的标记重新加载实体。不能应用于玩家。
 
### `modify(e,`nbt`,标记)`
 
从提供的标记重新加载实体。最好使用一个有效的实体标记,会出什么问题?想知道如果你
把兔子的大脑移植到村民身上?不能应用于玩家。
 
##实体事件
 
发生在实体上的事件有很多,您可以将自己的代码以事件处理程序的形式附加到这些实体上。
事件处理程序是在包中运行的接受某些预期参数的任何函数,您可以
用你自己的论点展开。当需要执行给定的命令时,它会这样做
如果它接受的参数数等于事件参数数,则在
使用`entity event`定义回调。
 
实体可以处理以下事件：
 
*``on tick``：在实体在游戏中被勾选之前执行每个勾选。必需参数：`entity`
*``on death``：当一个活着的实体死亡时执行一次。必需参数：`entity,reason`
*``on removed`：删除实体时执行一次。必需参数：`entity`
*``on damaged``：每当一个活着的实体即将受到伤害时执行。
必需参数：`entity,amount,source,u entity`
 
这并不意味着所有实体类型都有机会执行给定的事件,但实体不会出错
当你附加了一个不适用的事件。
 
如果要传递模块中未定义的事件处理程序,请阅读上的提示
`将函数引用传递给应用程序的其他模块`部分。
 
 
### `entity load handler(descriptor/descriptors,function)`,`entity load handler(descriptor/descriptors,call name。。。args?)`
 
在游戏中加载与以下类型匹配的任何实体时附加一个回调,允许获取句柄
当一个实体加载到世界上时,不必每次都查询它们。回调需要一个参数-实体。
如果回调为`null`,则删除当前的实体处理程序(如果存在)。对`entity load handler`的连续调用将进行加法/减法运算
当前目标实体类型池的。
 
与其他全局事件一样,对`entity load handler`的调用只能附加在具有全局作用域的应用程序中。球员范围使如此
不清楚使用哪个播放器来运行加载调用。
 
```
//摆脱所有僵尸的快速方法。回调太早了,它的数据包还没有到达客户端
//因此,为了保存日志错误,mob的删除需要安排在以后。
实体加载处理程序(`zombie`,(e)->调度(0,(outer(e))->修改(e,`remove`))
 
//使所有僵尸立即更快,更不容易受到任何摩擦
实体加载处理程序(`zombie`,(e)->实体事件(e,`on  tick`,(e)->修改(e,`motion`,1.2*e~`motion`))
```
 
注意：实体可以加载不同状态的块,例如在生成块时,这意味着
访问世界区块会导致游戏冻结,因为在生成区块时强制生成该区块。制造
千万不要假设块已经准备好了,并使用`entity load handler`围绕加载的实体安排操作,
或者直接操纵实体。
 
例如,下面的处理程序是安全的,因为它只直接访问实体。它能让所有产卵的猪人跳起来
```
/script run实体加载处理程序(`zombified  piglin`,(e)->修改(e,`motion`,0,1,0))
```
但是下面的处理程序,试图消灭在门户中产卵的猪人,将导致游戏冻结,因为对块的级联访问将导致相邻的块
强制生成,也会导致脚本删除实体后发送的数据包导致所有pigmen的错误消息。
```
/script run实体 load  handler(`zombified  piglin`,(e)->if(block(pos(e))==`nether  portal`,modify(e,`remove`))
```
规避这些问题的最简单方法是延迟检查,这可能会或可能不会导致级联负载发生,但是
一定会打破无限的链条。
```
/script run实体 load  handler(`zombified  piglin`,(e)->调度(0,(outer(e))->if(block(pos(e))==`nether  portal`,modify(e,`remove`))
```
但是最好的方法是在实体第一次被勾选的时候执行检查——让游戏一直处于被勾选状态以确保块
已完全加载并正在处理实体,正在删除tick处理程序
```
/script run实体加载处理程序(`zombified  piglin`,(e)->实体事件(e,`on  tick`,(e)->(if(block(pos(e))==`nether  portal`,modify(e,`remove`));实体事件(e,`on tick`,null)))
```
 
### `entity event(e,event,function)`,`entity event(e,event,call name。。。args?)`
 
附加当前包中的特定函数以调用`event`,并将额外的`args`带到
事件处理程序的原始必需参数。
 
<pre>
保护村民(实体,数量,来源,来源实体,治疗玩家)->
(
if(source entity&&source entity~`type`！=`玩家`,
修改(实体,`健康`,金额+实体~`健康`);
粒子(`端杆`,位置(实体)+l(0,3,0));
print(str(`%s治愈,感谢%s`,实体,治疗播放器))
)
);
__在\玩家\与\实体(玩家,实体,手)交互\>
(
if (entity~`type`==`村民`,
实体\事件(实体,`伤害`,`保护村民`,玩家~`名字`)
)
)
</pre>
 
在这种情况下,这将保护村民免受实体伤害(僵尸等),除非玩家授予所有
受伤后的村民恢复了健康。
#库存和物料API
 
##操纵块和实体的库存
 
这一类中的大多数函数都需要inventory作为第一个参数。库存可以由实体指定,
或者一个块,或者一个有库存的潜在块的位置(三个坐标),或者可以前面有库存
类型。
库存类型可以是`null`(默认),`enderchest`表示播放器enderchest存储,或`equipment`应用于
实体手和盔甲碎片。然后类型后面可以是实体,块或位置坐标。
例如,player enderchest inventory需要
两个参数,关键字``enderchest`,后跟player实体参数(或一个作为字符串的参数)
表格：``enderchest steve``表示传统支持)。如果你的球员名字以`enderchest`开头,首先是运气不好,
但它总是可以通过传球进入
实体值。如果所有其他方法都失败,它将尝试将前三个参数标识为
大批存货。玩家库存也可以通过他们的名字来调用。
 
一些生命实体可以同时拥有两种：它们的常规库存和它们的设备库存。
玩家的常规库存已经包含了装备,但是你也可以访问装备部分,以及
他们的最爱是分开的。对于只有
在他们的设备库存中,默认情况下返回设备(`null`类型)。
 
如果这让人困惑,请参阅`库存大小`下有关如何访问库存的示例。所有其他`inventory\…()`函数
使用相同的方案。
 
 
如果实体或块没有
在清单中,所有API函数通常都不做任何操作并返回null。
 
返回的大多数项都是项名称,计数和nbt的三元组形式,或者是与项关联的额外数据。
 
### `项目列表(标签?)`
 
不带参数,返回游戏中所有项目的列表。如果提供了项标记,则列出与该标记匹配的项,如果标记无效,则列出`null`。
 
### `item标签(item,tag?)`
 
返回项所属的标记列表,如果提供了标记,则返回`true`(如果项处理该标记),如果没有则返回`false`(如果该标记无效则返回`null`)
 
如果项目不存在,则抛出`unknown item`。
 
### `stack limit(项目)`
 
返回指示项的堆栈限制的数字。通常为1(不可堆叠),16(类似桶),
或者64-休息。建议参考这一点,因为其他inventory API函数忽略正常堆栈限制,并且
由程序员来控制它。从1.13开始,游戏检查负数并将一个项目设置为
负数等于空。
 
如果项目不存在,则抛出`unknown item`。
 
<pre>
堆叠限制(`木斧`)=>1
堆栈限制(`ender pearl`)=>16
堆叠限制(`石头`)=>64
</pre>
 
### `item类别(item)`
 
返回表示给定项的类别的字符串,如`building blocks`,`combat`或`tools`。
 
如果项目不存在,则抛出`unknown item`。
 
<pre>
物品类别(`木制斧头`)=>工具
物品分类(`ender pearl`)=>杂项
物品类别(`石头`)=>积木
</pre>
 
###`配方数据(项目,类型?)`,`配方数据(配方,类型?)``
 
返回匹配`item`或表示实际`recipe`名称的所有配方。在vanilla数据包中,用于所有项目
如果有一个可用的配方,则配方名称与项目名称相同,但如果一个项目有多个配方,则其
直接名称可以不同。
 
配方类型可以采用以下选项之一：
*``crafting``-默认,制作表配方
*`熔炼`-炉子配方
*`爆破`-高炉配方
*`吸烟`-吸烟者食谱
*`篝火烹饪`-篝火食谱
*`石刻`-石刻食谱
*``smithing``-锻造台(1.16+)
 
返回值是可用配方的列表(即使只有一个配方可用)。每一个食谱包含
制作结果的三重项,成分列表,每个都包含一个可能的材料变体列表
此槽中的配料,作为项目的三倍,或`空`,如果它是一个成型的配方和给定槽的模式是左
空,并将配方规范作为另一个列表。可能的配方规格为：
*`[`形`,宽,高]`-形工艺`width`和height`可以是1,2或3。
*`[`shapeless`]`-无形工艺
*`[`熔炼`,持续时间,xp]`-熔炼/烹饪食谱
*`[`切割`]`-石匠食谱
*`[`special`]`-特殊工艺配方,通常不在工艺菜单中
*`[`custom`]`-其他配方类型
  
请注意,配料是指定为牛肚,计数和nbt信息。目前所有的食谱都需要一个
对于某些配方,即使指定了配料的nbt数据(例如`分配器`),也会
可以接受任何标签的项目。
 
还要注意的是,有些配方会在工艺窗口中留下一些产品,这些可以使用
`制作剩余的 item()`function
  
示例：
<pre>
配方数据(`铁锭来自金块`)
配方数据(`铁锭`)
配方数据(`玻璃`,`熔炼`)
</pre>
 
### `crafting remaining item(物品)`
 
如果物品在制作窗口中没有剩余的物品作为制作原料使用,或者作为
制作完成后作为替代品的物品名称。目前只能是水桶和玻璃瓶。
 
### `库存大小(库存)`
 
返回有关实体或块的清单大小。如果块或实体不存在,则返回null
清点一下。
 
<pre>
库存大小(player())=>41
库存大小(`enderchest`,player())=>27//enderchest
库存大小(`装备`,player())=>6//装备
inventory_size(null,player())=>41//玩家的默认库存
 
库存大小(x,y,z)=>27//胸围
库存大小(块(位置))=>5//料斗
 
horse=spawn(`马`,x,y,z);
库存大小(马);=>2//默认马库存
库存大小(`装备`,马);=>6//未使用的马设备库存
库存大小(空,马);=>2//默认马
 
爬行动物=繁殖(`爬行动物`,x,y,z);
库存大小(爬行动物);=>6//默认爬行器库存为设备,因为它没有其他库存
库存大小(`设备`,爬行器);=>6//未使用的马设备库存
库存大小(空,爬行动物);=>6//爬行器默认为其设备
</pre>
 
### `inventory有个项目(inventory)`
 
如果库存不为空,则返回true;如果库存为空,则返回false;如果库存不是库存,则返回null。
 
<pre>库存有物品(player())=>真
库存有物品(x,y,z)=>假//空箱
库存物品(块(位置))=>空//石头
</pre>
 
### `inventory get(库存,插槽)`
 
返回相应库存槽中的项目,如果槽为空或库存无效,则返回null。你可以用
负数表示从`后面`开始计数的插槽。
 
<pre>
inventory get(player(),0)=>null//第一个热工具栏槽中没有任何内容
库存获取(x,y,z,5)=>[`stone`,1,{}]
inventory get(player(),-1)=>[`钻石镐`,1,{Damage:4}]//副手钻石镐轻微损坏
</pre>
 
### `inventory set(库存,插槽,计数,项目?,nbt?)`
 
修改或设置库存中的堆栈。指定计数0以清空插槽。如果未指定项,则保留现有项
项,只是修改计数。如果提供了项-替换当前项。如果提供了nbt,则在
在插槽处堆叠。返回该插槽中的上一个堆栈。
 
<pre>
inventory set(player(),0,0)=>[`stone`,64,{}]//玩家在第一个热工具栏槽中有一堆石头
inventory set(player(),0,6)=>[`diamond`,64,{}]//将玩家槽中的钻石堆栈更改为6
库存集(player(),0,1,`钻石斧`,`{Damage:5}`)=>null//在第一个玩家槽中添加了轻微损坏的钻石斧
</pre>
 
### `inventory find(inventory,item,start slot?,),库存 find(inventory,null,start slot?)`
 
查找在库存中具有相应项的第一个槽,如果使用null查询,则查找第一个空槽。
如果找到,则返回插槽号,否则返回null。可选的start slot参数允许跳过前面的所有插槽
允许对项目进行有效的(而不是逐槽)库存搜索。
 
<pre>
inventory find(player(),`stone`)=>0//玩家在第一个热键槽中有石头
inventory find(player(),null)=>null//玩家的库存没有空位
while((slot=inventory find(p,`diamond`,slot))！=null,41,drop项(p,slot))
//从玩家库存中吐出所有钻石
inventory_drop(x,y,z,0)=>64//移除并在世界上产生一堆物品
</pre>
 
如果项目不存在,则抛出`unknown item`。
 
### `inventory remove(库存,项目,金额?)`
 
从库存中删除项目的金额(默认为1)。如果库存没有定义的数量,则什么都没有
否则,给定数量的项目将被移除,无论它们在库存中的何处。返回布尔值
移除操作是否成功。从玩家库存中移除特定物品的最简单方法
不指定插槽。
 
<pre>
inventory_remove(player(),`diamond`)=>1//从玩家库存中移除钻石
inventory_remove(player(),`diamond`,100)=>0//玩家没有100颗钻石,什么都没有发生
</pre>
 
### `drop item(库存,插槽,金额?,)`
 
将物品从指定的库存槽中取出,比如玩家Q是物品或村民,交换食物。
您也可以从批量库存中查询项目。默认值为0-这是插槽中的所有值。
注意：漏斗足够快,可以从他们的库存中挑选所有排队的物品。
返回实际删除项目的大小。
 
<pre>
库存物品掉落(player(),0,1)=>1//Q是地面上的一件物品
inventory_drop(x,y,z,0)=>64//移除并在世界上产生一堆物品
</pre>
#Scarpet事件系统
 
Scarpet提供了在事件发生时执行特定函数的能力。为事件订阅的函数
需要符合事件规范的参数。当某些游戏中的
事件会发生,但应用程序设计者可以创建自己的事件,并在所有加载的应用程序中触发它们。
 
加载应用程序时,启动的每个函数
使用`u on u<event>`并具有必需的参数,将自动绑定到相应的内置事件`未定义的``ying
这样的函数将导致解除应用程序与此事件的绑定。
 
如果是`player`范围的应用程序,
所有玩家动作事件将被引导到相应的玩家主机。全局事件,比如`tick`,没有特定的
玩家目标将执行多次,每个玩家一次,在`全局`范围的应用程序中执行一次。
 
大多数内置事件都力争在它们在游戏中生效之前立即报告。这样做的目的是为了给你一个选择
让程序员立即处理它们(实际上,通过更改
环境),或者决定在它之后处理它,方法是在勾号结束时安排另一个调用。或两者兼而有之-
在事件发生之前部分地处理它,在事件发生之后处理其余的。但在某些情况下,这可能会导致程序员
混乱(比如处理重生事件仍然是指玩家的原始位置和维度),但是给了很多
更多地控制这些事件。
 
程序员还可以定义自己的事件,并向其他事件(包括内置事件)发送信号,以及跨所有加载的应用程序发送信号。
 
##事件列表
 
下面是scarpet中默认处理的事件列表。此列表包括函数名的前缀,允许应用程序
自动加载,但始终可以向任何事件添加任何函数(使用`/script event`命令)
如果它接受所需数量的参数。
 
##元事件
 
这些事件不是由游戏本身控制/触发的,但对应用程序的流程非常重要
意图和目的可以被视为常规事件。与常规事件不同,它们不能与`handle event`连接,
但应用程序本身需要将它们定义为不同的函数定义,同样,它们不能被通知。
 
### ` on  start()`
在每个应用程序的逻辑执行运行中调用一次。对于`global`作用域应用程序,它将在加载应用程序后立即执行。为了
``player``范围应用程序,在每个玩家可以使用该应用程序之前,它会被触发一次。因为每个玩家应用程序
独立于其他播放器应用程序,这可能是包含一些特定于播放器的初始化的最佳位置。静态
代码(即直接在应用程序代码中键入的代码,在函数定义之外立即执行)将只执行一次
每个应用程序,不管作用域如何,`` on  start()``允许可靠地调用特定于播放器的初始化。
 
### `u on close()`
 
在应用程序关闭或重新加载时,即在删除应用程序之前,为每个应用程序调用一次。
对于播放器范围内的应用程序,每个播放器调用一次。Scarpet应用程序引擎将尝试调用` on  close()`,即使
系统异常关闭。
 
 
##内置全球活动
 
对于每个具有`Global`作用域的应用程序,全局事件将处理一次。对于`player`范围的应用程序,每个player实例
独立于处理他们的事件,所以一个全局事件可以为每个玩家执行多次。
 
 
###`上的服务器启动()`
在加载world和所有启动应用程序启动后触发事件。它不会被`/reload`触发。
 
### ` on  server  shutdown()`
在执行` on close()`之前,服务器启动关闭进程时触发事件。不像` on close()`,它没有
用`/reload`触发。
 
### `u on  tick()`
事件触发器在每个勾号的开头,位于overworld中。可以使用`in dimension()`
从那里进入其他维度。
 
### `u on  tick  nether()`(已弃用)
重复的`tick`,只是自动位于虚空。在维度中使用` on  tick()->(`nether`,…`相反。
 
### ` on  tick  ender()`(已弃用)
重复的`tick`,只是自动位于结尾。在维度中使用` on  tick()->(`end`,…`相反。
 
###生成的块上的``(x,z)`
在给定坐标的块完全生成之后调用``x`和`z`对应
到块中最低的x和z坐标。事件可能(也可能不)与Optifine一起工作
同时。
 
### `u on  lightning(块,模式)`
闪电击中后立即触发。闪电实体以及潜在的骑士陷阱
在那一点上已经产生了`如果闪电确实导致陷阱产生,mode是true。
 
###爆炸(位置,电源,来源,原因,模式,火灾)`
 
在爆炸发生之前触发的事件对世界有任何影响`source`可以是导致
爆炸,以及引发爆炸的实体,
`mode`表示块效果：``none``,``break``(删除所有块),或``destroy``-删除几个块。事件
调用`create explosion()`时未捕获。
 
###爆炸结果(位置,电源,源,原因,模式,火,块,实体)`
在爆炸过程中触发,在对块进行任何更改之前,
但爆炸的决定已经做出,实体已经受到影响。
参数`blocks`包含将爆炸的块列表(如果`explosionNoBlockDamage`设置为`true`,则为空)。
参数`entities`包含受爆炸影响的实体列表。即使使用`create explosion()`也会触发。
 
### `u on rule更改(规则,新值)`
更改地毯模式规则时触发。它包括扩展规则,不使用默认的`/carpet`命令,
然后命名为`namespace:rule`.
 
###实体加载事件->检查`entity load handler()`
 
每当一个给定类型的实体被加载到游戏中时,它们就会触发：派生,添加一个块,
从命令中衍生出来的,真的。有关详细信息,请选中`实体`部分中的`实体加载处理程序()`。
 
##内置播放器事件
 
这些由播放器上下文触发。对于具有`player`作用域的应用程序,它们会为相应的
玩家。在具有`global`作用域的应用程序中,它们会触发一次全局事件。
 
### ` on  player使用项(player,item tuple,hand)`
使用右键单击操作触发。事件在服务器接收到数据包之后,在
游戏设法做任何事。当玩家开始进食或开始拉弓时触发事件。
使用`player finishes using item`或`player releases item`捕获这些事件的结束。
 
当玩家放置一个方块时,事件不会被触发
`玩家右键单击`阻止`或`玩家放置阻止`事件。
 
### ` on  player释放项(player,item tuple,hand)`
玩家停止右击一个可以被持有的物品。此事件是客户端请求的结果。
可能导致这种情况发生的例子是释放弓。事件是在游戏进程之后触发的
但是,请求中提供的`item tuple`表示播放器开始使用的项。你可以用它
与增量的当前保留项进行比较。
 
### ` on  player使用 item(player,item tuple,hand)完成`
玩家使用物品完成。这是由服务器端控制的,负责诸如finishing之类的事件
吃东西。在确认操作有效并将反馈发送回后触发事件
但是在触发它和它在游戏中的效果之前。
 
### `u on  player 单击 block(player,block,face)`
表示对块的左击攻击,通常表示块开始被破坏。在服务器之后触发
在服务器端发生任何事情之前接收客户端数据包。
  
 
### `u on  player 打破块(player,block)`
当玩家在对世界进行任何改变之前打破一个方块时调用,但决定移除方块。
 
### `在播放器上右键单击块(播放器,项目元组,手,块,面,hitvec)`
当玩家用鼠标右键单击某个块或与某个块交互时调用。此事件被正确触发
在其他交互事件之前,例如`player interactions with block`或`player places block`。
 
### `u on  player 与 block交互(player,hand,block,face,hitvec)`
当玩家成功地与一个区块交互,从而激活该区块时调用,
就在这之后。
  
### `u on  player  places  block(播放器,项目元组,手牌,块)`
当玩家放置一个挡块时触发,挡块放置在世界上之后,但在计分板触发或玩家库存之前
已调整。
 
###玩家与实体(玩家,实体,手)交互`
当玩家右键单击(交互)一个实体时触发,即使该实体与玩家或用户没有普通交互
他们手里的东西。在从客户端接收到数据包之后,在服务器端发生任何事情之前调用该事件
有了这种互动。
 
### `u on  player  trades(玩家,实体,买左,买右,卖)`
当玩家与商家交易时触发。在服务器允许交易之后,但在库存之前调用该事件
更改和商户更新其交易用途柜台。
如果商户不是实体,参数`entity`可以是`null`。
 
###`玩家上的`与实体(玩家,实体)碰撞`
每次计算玩家-实体碰撞时触发,然后在游戏中应用碰撞效果。
不仅在与生命实体碰撞时有用,而且在物品或经验球产生效果之前拦截它们
在球员身上。
 
### `u on  player 选择配方(player,recipe,full  stack)`
在服务器收到消息后,当玩家从工艺手册中单击工艺窗口中的配方时触发
客户端请求,但在将任何项目从其库存移动到制作菜单之前。
 
### ` on  player切换插槽(player,from,to)`
当玩家更改其选定的热键槽时触发。在服务器收到要切换的消息后立即应用
吃角子老虎机。
 
### `u on  player 交换手(player)`
当玩家发送命令交换他们的即兴物品时触发。在服务器上应用效果之前执行。
 
### `u在玩家挥动手(玩家,手)`
当玩家开始挥手时触发。事件通常在导致它的相应事件之后触发
(`player uses item`,`player breaks block`,等等),但是在一些失败的事件之后,它也会触发,比如攻击空中。什么时候?
摆动作为动作的效果继续,在摆动停止之前不会发出新的摆动事件。
 
### ` on  player 攻击实体(player,entity)`
当玩家攻击实体时触发,就在它发生在服务器端之前。
 
### ` on  player 承受伤害(player,amount,source,source 实体)`
当玩家受到伤害时触发。事件在应用潜在吸收之后和之前立即执行
实际伤害将应用于玩家。
 
###`玩家`造成伤害(玩家,数量,实体)`
当玩家对另一个实体造成伤害时触发。如果两者同时发生,则其作用与`玩家承受伤害`相同
事件的双方都是玩家,和其他实体类似,只是他们的吸收被采取了两次,只是从来没有人
注意到¯\_(ツ)_/¯
 
### `u on player死亡(player)`
玩家死亡时触发。玩家已经死了,所以不要复活他们。广播消息前应用的事件
关于玩家死亡和应用外部效果(如暴徒愤怒等)。
 
### `\\\\\\\玩家\重生(玩家)`
当玩家重生时触发。这包括死后产卵,或离开终点后登陆超世界。
当事件被处理时,玩家仍在其先前的位置和维度-将在之后重新定位。在
案例玩家死后,其之前的库存已经被分散,而其当前的库存不会被复制到重生
实体,所以对播放器数据的任何操作都是
最好安排在勾选结束时,但您仍然可以使用其当前引用来查询其在重新启动事件时的状态。
 
### ` on  player更改维度(player,from  pos,from  dimension,to  pos,to  dimension)`
当玩家从一个维度移动到另一个维度时调用。当玩家处于前一个状态时,事件仍被处理
尺寸和位置。
 
`当游戏者从结尾回到超世界时,游戏者改变维度返回空值
,因为玩家重生的位置不受传送的控制,或者玩家仍然可以看到结束的点数。之后
玩家有资格在超世界重生,将触发`玩家重生`。
 
### `\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\`
当服务器在驾驶车辆时接收到移动控制时触发。在应用效果之前对其进行处理
服务器端。
 
### `u on  player 跳跃(player)`
当游戏接收到来自客户端的跳转输入时触发,玩家被视为站在地上。
 
 
### `u on  player 部署 elytra(player)`
当服务器收到部署elytra的请求时触发,不管航班是否在服务器端商定。。
 
### `u on  player唤醒(player)`
玩家在睡梦中从床上醒来,但不是因为他已经睡过了才被踢下床。
 
### `u on  player 逃避睡眠(player)`
与`播放器唤醒`相同,但仅在按ESC按钮时触发。不知道莫江为什么要送这个活动
两次按escape,但能检测到可能很有趣。
 
### `u on  player 开始偷袭(player)`
### `u on  player 停止偷袭(player)`
### `u on  player 启动冲刺(player)`
### `在玩家停止冲刺(玩家)`
当玩家控制潜行和冲刺时触发四个事件。
 
### `u on  player 丢弃物品(player)`
### ` on  player  drops  stack(播放器)`
当游戏收到玩家请求从其库存中删除一个物品或整堆物品时触发。
事件发生在服务器端发生任何更改之前。
 
### `u on  player 拿起物品(player,物品)`
在玩家成功摄取库存物品后触发。Item表示项的总堆栈
被玩家吞下。这些物品的确切位置在技术上是未知的
物品可以分散在整个库存中。
 
### ` on  player连接(player)`
当玩家成功登录并进入游戏时触发。
 
### ` on  player断开连接(player,reason)`
当播放机发送断开连接包或强制断开与服务器的连接时触发。
 
### `uu on统计(玩家,类别,事件,值)`
当玩家统计数据改变时触发。不通知周期性的有节奏的事件,即。
``死亡后的时间`,`休息后的时间`和`玩了一分钟`,因为这些都是在每一个滴答声中触发的。事件
在更改这些统计信息的计分板值之前处理。
 
## 自定义事件和入侵scarpet事件系统
 
应用程序程序员可以定义和触发自己的自定义事件。与内置事件不同,所有自定义事件都传递一个值
但这并不意味着它们不能将复杂的列表,映射或nbt标记作为消息传递。每个事件信号
或者针对所有玩家的所有应用实例,包括全局应用,如果没有确定目标玩家,
或者只有玩家范围的应用,如果目标玩家
指定,为该播放器应用程序运行一次。你不能以玩家为目标信号的全球应用为目标。内置事件
不要瞄准全球应用程序,因为它们的第一个参数是明确定义和传递的。如果有可能的话,这种情况在未来可能会改变
一个令人信服的论据,能够针对全球应用与球员范围。
 
程序员还可以像处理自定义事件一样处理内置事件,也可以触发内置事件,我已经介绍了
我不知道你为什么需要那个。以下代码段具有相同的效果：
 
```
__在\玩家\断开\块(玩家,块)->print(玩家+`断开`+块);
```
和
```
handle_event(`player_breaks_block`,_(player,block)->print(player+`break`+block));
```
 
以及
```
未定义(```````````````;
```
和
```
handle event(`player breaks block`,null);
```
`signal_event`可以用作触发器,对于基于播放器的内置事件调用两次
```
信号_事件(`player_breaks_block`,player,player,block);//以所有播放器范围的应用为目标
signal_event(`player_breaks_block`,null,player,block);//以所有全局范围的应用程序和所有播放器实例为目标
```
或(全球活动)
```
signal_event(`tick`)//触发所有有tick事件的应用程序
```
 
### `handle event(事件,回调…)`
 
提供由``event``参数标识的事件的处理程序。如果事件尚不存在,则将创建它。
所有全局加载的应用程序都可以在调用相应的`signal event(event,…)`时触发该事件。回调可以是
定义为函数名,函数值(或lambda函数)以及将传递的可选额外参数
当事件被触发时。所有自定义事件都需要一个函数,该函数接受一个由事件触发器传递的自由参数。
如果提供了额外的参数,它们将被附加到回调函数的参数列表中。
 
如果对事件的订阅成功,则返回`true`;如果订阅失败,则返回`false`(例如,内置事件的作用域错误,
或不正确的事件参数数)。
 
如果回调被指定为`null`,则给定的应用程序(或播放器应用程序实例)将停止处理该事件。
 
<pre>
foo(a) -> print(a);
handle_event('boohoo', 'foo');

bar(a, b, c) -> print([a, b, c]);
handle_event('boohoo', 'bar', 2, 3) // using b = 2, c = 3, a - passed by the caller

handle_event('tick', _() -> foo('tick happened')); // built-in event

handle_event('tick', null)  // nah, ima good, kthxbai
</pre>
 
如果要传递模块中未定义的事件处理程序,请阅读上的提示
`将函数引用传递给应用程序的其他模块`部分。
 
 
### `signal_event(event, target_player?, ... args?)`
 
激发特定事件。如果事件不存在(仅`handle event`创建缺少的新事件),或提供了参数列表
与被调用者期望的参数不匹配,返回`null`,
否则返回通知的应用程序数。如果指定了`target player`,而不是`null`,则触发特定于播放器的事件,targeting
只有`player`范围内的应用程序才适用于该播放器。具有globals作用域的应用程序即使处理此事件也不会收到通知。
如果省略`target player`或`null`,它将针对`global`范围的应用程序和`player`范围的应用程序的所有实例。
请注意,所有内置的player事件都有一个player作为第一个参数,因此要触发这些事件,您需要
提供它们两次—一次指定目标播放器范围,第二次—作为处理函数的参数提供。
 
<pre>
信号_事件(`player_breaks_block`,player,player,block);//以所有播放器范围的应用为目标
signal_event(`player_breaks_block`,null,player,block);//以所有全局范围的应用程序和所有播放器实例为目标
signal_event(`tick`)//触发所有有tick事件的应用程序
</pre>
 
## 自定义事件示例
 
下面的示例演示如何在同一播放器范围的应用程序的不同实例之间进行通信。值得注意的是
如果事件名称匹配,该信号也可以触发其他应用程序。在这种情况下,将调用请求名称
`tpu请求`并由命令触发。
 
 
``` 
// tpa.sc
global_requester = null;
__config() -> {
	'commands' -> {
		'<player>' -> _(to) -> signal_event('tp_request', to, player()),
      'accept' -> _() -> if(global_requester, 
         run('tp '+global_requester~'command_name'); 
         global_requester = null
      )
	},
   'arguments' -> {
      'player' -> {'type' -> 'players', 'single' -> true}
   }
};
handle_event('tp_request', _(req) -> (
   global_requester = req;
   print(player(), format(
      'w '+req+' requested to teleport to you. Click ',
      'yb here', '^yb here', '!/tpa accept',
      'w  to accept it.'
   ));
));
```
 
## `/script event`命令
 
用于显示当前事件和有界函数。使用`add to`注册新事件,或使用`remove from`注册新事件
从事件中解除特定函数的绑定。要绑定到事件的函数需要具有相同数目的
操作试图绑定到的参数(请参见上面的列表)。通过`/script load`加载的模块中的所有调用
处理特定内置事件的脚本将自动绑定,并在卸载脚本时不绑定。
# 计分板
 
### `scoreboard()`, `scoreboard(objective)`, `scoreboard(objective, key)`, `scoreboard(objective, key, value)`
 
显示或修改单个计分板值。不带参数,返回当前目标的列表。
如果指定了`objective`,则列出与当前目标关联的所有键(玩家),如果目标不存在,则列出`null`。
具有指定的`objective`和
`key`,返回给定玩家目标的当前值(key)。使用附加的`value`设置新的计分板
值,返回与`key`关联的上一个值。如果`value`为空,则重置记分板值。
 
### `scoreboard_add(objective, criterion?)`
 
向记分板添加新目标。如果未指定`criteria`,则假定为`dummy`。
如果已创建目标,则返回`true`;如果已存在具有指定名称的目标,则返回`null`。
 
如果条件不存在,则抛出`unknown_criterion`。
 
<pre>
scoreboard_add('counter')
scoreboard_add('lvl','level')
</pre>
 
### `scoreboard remove(目标)``scoreboard remove(目标,键)`
 
删除整个目标或记分板中与该键相关的条目。
如果目标已存在且已删除,或以前存在,则返回`true`
如果球员得分被移除,计分板的值。如果目标不存在或缺少键,则返回`null`
为了目标。
 
### 计分板显示(地点,目标)`
 
设置指定`目标`的显示位置。如果`objective`为`null`,则清除显示。如果目标无效,
返回`null`。
 
### `scoreboard property(objective,property)``scoreboard property(objective,property,value)`
 
读取`objective`的属性或将其设置为`value`(如果指定)。可用属性包括：
 
*`标准`
*`display name`(支持格式化文本)
*`display_slot`：读取时,返回显示此目标的插槽列表;修改时,显示指定插槽中的目标
*`render type`:integer`或hearts`,如果指定的值无效,则默认为integer`
 
#团队
 
### `team list()`,`team list(团队)`
 
以不带参数的列表形式返回所有可用的团队。
 
当指定了`team`时,它将返回该团队中的所有玩家。如果`team`无效,则返回`null`。
 
### `team add(团队)`,`team add(团队,玩家)`
 
使用一个参数,创建一个新的`team`,如果成功,则返回其名称;如果team已经存在,则返回`null`。
 
 
`team_add(`admin`)`->创建一个名为`admin`的团队
`team_add(`admin`,`Steve`)`->将玩家`Steve`加入到团队`admin`
 
如果指定了`player`,则该玩家将加入给定的`team`。如果玩家加入了团队,则返回`true`,如果因为玩家已经加入了团队而没有任何更改,则返回`false`。如果团队无效,则返回`null`
 
### `team删除(team)`
 
删除`team`。如果团队被删除,则返回`true`;如果团队无效,则返回`null`。
 
### `U队离开(球员)`
 
将`player`从他所在的团队中删除。如果球员离开球队,则返回`true`,否则返回`false`。
 
`team leave(`Steve`)`->将Steve从当前所在的团队中删除
`对于(team_list(`admin`),team_leave(`admin`,匼))`->从team`admin`中删除所有玩家
 
### `team属性(team,property,value?)`
 
如果未指定`value`,则读取`team`的`property`。如果将`value`作为第三个参数添加,则会将`property`设置为该`value`。
 
*`碰撞规则`
*类型：字符串
*选项：总是,从不,推其他团队,推下团队
    
*`颜色``
*类型：字符串
*选项：请参见[团队命令](https://minecraft.gamepedia.com/Commands/team#Arguments)(与``teamcolor``[命令参数]相同的字符串)(https://github.com/gnembon/fabric-carpet/blob/master/docs/scarpet/Full.md#command-参数类型(选项)
 
*`显示名称`
*类型：String或FormattedText,查询时返回FormattedText
  
*`前缀``
*类型：String或FormattedText,查询时返回FormattedText
 
*`后缀`
*类型：String或FormattedText,查询时返回FormattedText
 
*`友谊之火``
*类型：布尔型
  
*`See Friendly隐形人`
*类型：布尔型
  
*`NAMETAG可见性`
*类型：字符串
*选项：始终,从不,隐藏其他团队,隐藏其他团队
 
*`死亡信息可见性`
*类型：字符串
*选项：始终,从不,隐藏其他团队,隐藏其他团队
 
示例：
 
```
team属性(`admin`,`color`,`dark red`)使团队颜色为`admin`暗红色
team|property(`admin`,`prefix`,format(`r admin |`)设置`admin`中所有玩家的前缀
团队属性(`admin`,`display name`,`Administrators`)为团队`admin`设置显示名称
团队属性(`admin`,`seeFriendlyInvisibles`,true)使`admin`中的所有玩家即使在不可见时也能看到其他管理员
团队属性(`admin`,`deathMessageVisibility`,`hideForOtherTeams`)使`admin`中的所有玩家即使在不可见的情况下也能看到其他管理员
```
 
## `bossbar()`,`bossbar(id)`,`bossbar(id,属性,值?)`
 
像使用`/bossbar`命令一样管理bossbar。
 
不带任何参数,返回所有bossbar的列表。
 
当指定了一个id时,创建一个具有该id的bossbar,并返回所创建bossbar的id。
Bossbar id需要一个名称空间和一个名称。如果没有指定名称空间,它将自动使用`minecraft:`。
在这种情况下,您应该使用`bossbar(id)`返回的id跟踪bossbar,因为名称空间可能会自动添加。
如果id无效(例如有多个冒号),则返回`null`。
如果bossbar已经存在,则返回`false`。
 
`bossbar(`计时器`)=>`minecraft:timer``(添加名称空间`minecraft:`,因为没有指定任何名称空间)
 
`博斯巴尔scarpet:test`) => `scarpet:test``在本例中,已经指定了一个命名空间
 
`博斯巴尔foo:bar：baz`)=>null`标识符无效
 
`bossbar(id,property)`用于查询bossbar的`property`。
 
`bossbar(id,property,value)`可以将bossbar的`property`修改为指定的`value`。
 
可用属性包括：
 
*颜色：可以是`粉色`,`蓝色`,`红色`,`绿色`,`黄色`,`紫色`或`白色``
 
*样式：可以是`进度`,`缺口6`,`缺口10`,`缺口12`或`缺口20``
 
*价值：博斯巴尔进程的价值
 
*max:bossbar进程的最大值,默认为100
 
*名称：显示在bossbar上方的文本,支持格式化文本
 
*可见：bossbar是否可见
 
*玩家：可以看到bossbar的玩家列表
 
*添加玩家：将玩家添加到可以看到这个bossbar的玩家中,这个只能用于修改(`value`必须存在)
 
*删除：删除此bossbar,不需要`value`
 
```
博斯巴尔script:test`,`style`,`notched 12`)
博斯巴尔script:test`,`值`,74)
博斯巴尔script:test`,`name`,format(`rb Test`)->更改文本
博斯巴尔script:test`,`visible`,false)->移除可见性,但保留玩家
博斯巴尔script:test`,`players`,player(`all`))->对所有玩家可见
博斯巴尔script:test`,`players`,player(`Steve`)->仅对Steve可见
博斯巴尔script:test`,`players`,null)->无效的播放器,删除所有播放器
博斯巴尔script:test`,`add player`,player(`Alex`))->将Alex添加到可以看到bossbar的玩家列表中
博斯巴尔script:test`,`remove`)->删除bossbar`script:test`
对于(bossbar(),bossbar(216;,`remove`))->删除所有bossbar
```
 
 
 
 
#辅助方面
 
其他方法的集合,这些方法控制游戏中较小但仍然重要的方面
 
##声音
 
### `sound()`,`sound(名称,位置,音量,音调,混音器?)`
 
在块或位置`pos`播放特定的声音`name`,可选择`volume`和修改的`pitch`,并在下面播放
可选`mixer`。`volume`,`pitch`和`mixer`的默认值为`1.0`,`1.0`和`master`。
有效的混音器选项有`master`,`music`,`record`,`weather`,`block`,`敌对`,`中立`,`player`,`ambient``
还有`声音``pos`可以是一个块,三个坐标或一个数字列表。使用与
相应的`playsound`命令。
 
不带参数使用时,返回可用声音名称的列表。
 
如果声音不存在,则抛出`unknown sound`。
 
##粒子
 
### `particle()`,`particle(名称,位置,计数?)?。传播?,速度?,玩家?)`
 
渲染以`pos`位置为中心的`name`粒子云,默认为`count`10个,默认为`speed`
0和附近的所有玩家,但这些选项可以通过可选参数更改。跟随香草`/颗粒`
这些选项的详细信息。有效的粒子名是
例如`愤怒的村民`,`物品钻石`,`块石头`,`灰尘0.8 0.1 0.1 4`。
 
不带参数使用时,返回可用粒子名称的列表。请注意,有些名称与有效的
由于某些粒子需要更多配置,因此可以馈送到`particle(…)`函数的粒子
要有效,如`dust`,`block`等只能用作参考。
 
如果粒子不存在,则抛出`unknown particle`。
 
### `particle line(名称,位置,位置2,密度?,播放器?)`
 
使用提供的密度(默认值为1)渲染从点`pos`到`pos2`的一行粒子,该密度表示距离
分开你会希望粒子出现,所以`0.1`意味着每10厘米出现一个。如果提供了播放器(或播放器名称),则仅
该玩家将接收粒子。
 
如果粒子不存在,则抛出`unknown particle`。
 
### `particle box(名称,位置,位置2,密度?,播放器?)`
### `particle\ rect`(已弃用)
 
使用提供的密度渲染点`pos`和`pos2`之间的粒子长方体。如果玩家(或玩家名)是
提供,只有该玩家将接收粒子。
 
如果粒子不存在,则抛出`unknown particle`。
 
##标记
 
### `draw_shape(形状,持续时间,键?,值?,…)`,
### `draw_shape(形状,持续时间,l(键?,值?,…)`,
### `draw shape(形状,持续时间,属性映射)`
### `draw shape(形状列表)`
 
在世界中绘制一个将在`duration`记号后过期的形状。形状的其他属性应如下所示：
连续的键值参数对,要么作为下一个参数,要么打包在列表中,要么作为正确的键值提供
`地图`。参数可以包括共享的形状属性,这些属性都是可选的,以及特定于形状的属性
可以是可选的,也可以是必需的。所有地毯客户都会正确绘制形状。其他有联系的玩家
安装地毯后仍能看到所需形状的粉尘颗粒。替换形状
不需要精确地遵循所有属性,但可以让普通客户获得您的一些经验
应用程序。其中一个属性肯定不会被尊重的是持续时间-粒子将被发送一次
每一个形状和最后他们通常在游戏中最后。
 
可以使用前三个调用中的任何一个逐个发送形状,也可以作为形状描述符列表进行批处理。
批处理的好处是,它们可能作为一个数据包发送,从而限制了数据包的网络开销
同时发送许多小包来绘制几个形状。发送形状的缺点是,它们需要处理
相同的玩家列表,即如果列表中的多个玩家针对不同的玩家,则所有形状都将发送给所有玩家。
 
如果不是所有必需的参数,形状将无法绘制并引发运行时错误
并且所有可用的形状都有一些必需的参数,因此请确保将它们放置到位：
 
在客户机上,形状可以识别它们正在使用相同的参数重新绘制,而忽略
持续时间参数。这会将所绘制形状的过期时间更新为新值,而不是在其中添加新形状
地点。这可用于切换之前发送的持续时间非常长的形状,
或者在更动态的应用程序中周期性地刷新形状。
 
可选共享形状属性：
*‘color’——整数值,表示形状的主颜色,以红色,绿色,蓝色和alpha分量的形式表示
格式为`0xRRGGBBAA`,默认值为`-1`,因此为白色不透明,或`0xffffff`。
*`player`-要向其发送形状的名称或播放器实体,或播放器列表。如果指定,则形状将仅针对指定的
玩家(不管他们在哪里),否则将发送给当前维度中的所有玩家。
*`line`-(已弃用)线条粗细,默认为2.0pt。在1.17的3.2核心GL渲染器中不受支持。
*`fill`-面的颜色,默认为`no fill`。使用`颜色`属性格式
*`follow`-实体或播放器名称。形状将跟随实体而不是静态的。
Follow属性要求所有位置参数都与实体相关,并且不允许
使用实体或块作为位置标记。必须将位置指定为三元组。
*`snap`-如果存在`follow`,则指示在哪个轴上捕捉实体坐标,以及在哪个轴上捕捉实体坐标
将被静态处理,即在坐标三元组中传递的坐标是世界上的实际值。违约
值为``xyz`,这意味着形状将在所有三个方向上相对于实体绘制。使用`xz`表示
实例使形状跟随实体,但保持在相同的绝对Y坐标。在轴心之前
使用`d`,比如`dxdydz`,将使实体位置离散处理(向下舍入)。
 
可用形状：
*`直线`-在两点之间画一条直线。
*必需属性：
*`from`-表示行一端的三坐标,实体或块值
*`to`-行的另一端,格式与`from`相同`
     
*`label`-在世界上绘制文本。默认的`line`属性控制主字体颜色。
`fill`控制背景的颜色。
*必需属性：
*`pos`-位置
*`text`-要显示的字符串或格式化文本
*可选属性
*`value`-要显示的字符串或格式化文本,而不是主`text``值`INTERNATIONS`text`
不用于确定绘制文本的唯一性,因此可以用于
在元素值不断变化的情况下,平滑显示动态元素
正在从服务器发送对其的更改和更新。
*`size`-浮动。默认字体大小为10。
*`facing`-文本方向,它的朝向。可能的选项有：`player`(默认,文本
始终旋转以面对玩家),‘北’,‘南’,‘东’,‘西’,‘上’,‘下’`
*`doublesided`-如果`true`,它将使文本从后面也可见。默认值为`false`(1.16+)
*`align`-相对于`pos`的文本对齐方式。默认值为`center`(显示的文本为
相对于`pos`居中,`left`(`pos`表示文本的开头)和`right`(`pos`)`
表示文本结尾)。
*‘倾斜’,‘倾斜’,‘转动’——画布上文本沿所有三个轴的附加旋转
*`indent`,`height`,`raise`—X轴(`indent`),Y轴(`height`)和Z轴(`raise`)上文本呈现的偏移量
关于文本的平面。其中一个单位对应于1行间距
可用于显示绑定到同一`pos`的多行文本
     
*`box`-在指定的点上绘制具有角的长方体
*必需属性：
*`from`-表示方框一角的三坐标,实体或块值
*`to`-其他角,格式与`from`相同`
 
*`球体`：
*必需属性：
*‘center’——球的中心
*`radius`-球体的半径
*可选属性：
*`level`—详细程度,或网格大小。你的球体密度越大。默认级别为0,表示
详细等级将根据半径自动选择。
      
*`圆柱体`：
*必需属性：
*`center`-底座的中心
*‘radius’—基圆的半径
*可选属性：
*`轴`-圆柱体方向,``x``,``y``,``z``中的一个默认为``y``
*`height`-圆柱体的高度,默认为`0`,所以是平面圆盘。可能是阴性。
*`level`-详细级别,请参见`sphere`。
 
### `创建标记(文本,位置,旋转?,块?,交互式?)`
 
在位置处生成具有文本或块的(永久)标记实体。返回该实体以进行进一步操作。
卸载产生这些标记的应用程序将导致世界上已加载部分的所有标记被删除。
另外,如果游戏在将来加载了这个标记,而应用程序没有加载,它也会被删除。
 
如果`interactive`(默认情况下为`true`)是`false`,则armorstand将是一个标记,在任何情况下都不会是交互式的
游戏模式。但是块可以放在标记中,不会捕捉任何交互事件。
 
将调整标记文本或块的Y位置,使块或文本显示在指定位置。
这使得实际的装甲支架位置可以在Y轴上偏移。您需要调整您的实体
如果你打算在事后移动防具架的话。如果同时指定了文本和块-其中一个
将对齐(军械架类型标记文字显示在其脚下,而对于常规军械架-在头部上方,
而头部上的块始终在同一位置渲染(无论其是否为标记)。
 
 
### `删除所有标记()`
 
从这个应用程序创建的世界的加载部分删除所有scarpet标记,以防你不想这样做
正确的清理。
 
##系统功能
 
### `nbt(expr)`
 
将参数视为nbt可序列化字符串并返回其nbt值。如果nbt不是正确的nbt
复合标记格式,它将返回`null`值。
 
请参阅`Expression`中有关容器操作的部分,以了解对nbt值的可能操作。
 
### `转义(expr)`
 
对字符串或nbt标记中的所有特殊字符进行抽取,并返回一个可以直接存储在nbt中的字符串
作为字符串值。
 
### `标记匹配项(爸爸标记,宝宝标记,匹配项列表?)`
 
如果`baby tag`完全包含在`daddy tag`中,则返回`true`的实用程序。任何匹配`null`婴儿标签的内容,以及
`null`标记中不包含任何内容。如果指定了`match lists`和`false`,则忽略嵌套列表的内容。
默认行为是匹配它们。
 
### `parse nbt(标记)`
 
将NBT标记转换为scarpet值,您可以更好地浏览该值。
 
转换：
-使用字符串键将标记复合到地图中
-将标记列为列表值
-将数字(整数,浮点,双精度,长精度)转换为一个数字
-Rest转换为字符串。
 
### `编码(expr,force?)`
 
将表达式的值编码为NBT标记。默认情况下(或`force`为false时),它只允许
将结果标记应用于`parse_nbt()`时,对保证返回相同值的值进行编码。
可以可靠地在NBT值之间来回转换的支持类型包括：
-带字符串关键字的映射
-相同类型的项目列表(如果可能,scarpet将负责统一值类型)
-数字(根据需要编码为int->long->double)
-字符串
 
如果`force`为true,则其他值类型将仅转换为标记(包括NBT标记)。他们需要
从NBT加载时的额外处理,但使用`force`true将始终产生输出/从不产生输出
产生异常。
 
### `print(expr)`,`print(player/player\列表,expr)`
 
向聊天室显示表达式的结果。重写将所有内容发送到stderr的默认`scarpet`行为。
可以选择定义要向其发送消息的播放机或播放机列表。
 
### `format(components,…)`,`format(l(components,…))`
 
创建一行格式化文本。每个组件都是一个字符串,表示其对应的格式和文本
或者影响它前面的组件的装饰器。
 
常规格式组件是具有以下结构的字符串：
``<format><text>`,类似`gi Hi`,在本例中表示灰色斜体字`Hi`。分隔格式和文本的空间是必需的。格式可以为空,但空间仍然为空
需要有,否则第一个字的文本将被用作格式,没有人想要。
 
格式是表示格式的格式符号列表。他们可以混合和匹配,虽然颜色只会
申请一次。可用符号包括：
*`i`-斜体
*`b`-**粗体**
*`s`-~~删除线~~
*`u`-<u>下划线</u>
*`o`-模糊
 
颜色：
*`w`-白色(默认)
*`y`-黄色
*`m`-洋红色(浅紫色)
*`r`-红色
*`c`-青色(浅绿色)
*`l`-石灰
*`t`-浅蓝色
*`f`-深灰色(奇怪的弯曲,但还行)
*`g`-灰色
*`d`-黄金
*`p`-紫色
*`n`-棕色(暗红色)
*`q`-绿松石色(深绿色)
*`e`-绿色
*`v`-海军蓝
*`k`-布莱克
*`#FFAACC`-任意RGB颜色(1.16+),十六进制表示法。A-F符号使用大写
 
装饰器(列为它们将影响的组件后面的额外参数)：
*`^<format><text>``-将鼠标悬停在工具提示文本上,当鼠标悬停在下面的文本上时出现。
*``?<suggestion>`-命令建议-单击下面的文本时将粘贴到聊天室的消息。
* ``!<message>``-单击下面的文本时将执行的聊天信息。
 
建议和消息都可以包含一个命令,该命令将作为单击它的播放器来执行。
 
到目前为止,格式化文本的唯一用法是使用`print`命令。否则它的功能就像一个正常的
表示屏幕上实际显示内容的字符串值。
 
用法示例：
<pre>
print(format(`rbu Error:`,`r Stuff happened！`))
print(格式(`w Click`,`tb[HERE],`^di Awesome！`,`/杀死`,`w\按钮赢得1000美元`)
//我将第二个空格反斜杠的原因是,否则命令解析器可能会收缩连续的空格
//在应用程序中不是问题
</pre>
 
### `display title(players,type,text?,fadeInTicks?,stayTicks?,fadeOutTicks)`
 
向播放机(如果`players`是列表,则为播放机)发送特定类型的标题,可以选择某些时间。
*`players`是在线播放器或播放器列表。当发送单个播放器时,如果播放器无效或脱机,它将抛出。
*`type`是`title`,`subtitle`,`actionbar`或`clear`。
注意：只有在显示标题时才会显示`subtitle`(可以是空标题)
*`title`是发送给玩家的标题。除`clear`类型外,它是必需的。可以是使用`format()格式化的文本`
*`…Ticks`是标题将保持在该状态的记号数。
如果未指定,它将使用当前默认值(这些默认值可能已从以前的`/title times`执行更改)。
用这些执行将把时间设置为指定的时间。
注意,`actionbar`类型不支持更改时间(vanilla bug,请参见[MC-106167](https://bugs.mojang.com/browse/MC-106167)).
 
### `display title(players,`player list header`,文本)`
### `display title(players,`player list footer`,文本)`
 
更改指定目标的玩家列表的页眉或页脚。
如果`text`为`null`或空字符串,它将删除指定目标的页眉或页脚。
如果玩家运行地毯记录器,Scarpet指定的页脚将出现在记录器的上方。
 
### `logger(msg),logger(type,msg)`
 
将消息打印到系统日志,而不是聊天。
默认情况下,打印信息,除非在`type`参数中另有指定。
 
可用输出类型：
 
`'debug'`, `'warn'`, `'fatal'`, `'info'` 和 `'error'`
 
 
### `read_file(resource, type)`
### `delete_file(resource, type)`
### `write_file(resource, type, data, ...)`
### `list_files(resource, type)`
 
使用scripts文件夹中指定的`resource`(属于特定的`type`),向其写入/附加`data`,读取其
内容,删除资源,或列出此资源下的其他文件。
 
资源由文件的路径标识。
路径可以包含字母,数字,字符`-`,`+`或`\`,以及文件夹分隔符：`/`。任何其他字符都会被剥离
从名字开始。空描述符无效,除非`list files`表示根文件夹。
不要将文件扩展名添加到描述符-扩展名是推断出来的
基于文件的`type`。一个路径可以有一个`.zip`组件,表示允许读/写的zip文件夹
zip文件,但不能在其他zip文件中嵌套zip文件。
 
资源可以位于特定于应用程序的空间中,也可以位于所有应用程序的共享空间中。访问特定应用程序
资源保证与其他应用程序隔离。共享资源是。。。所有猿类都有,意思是
它们可以吃掉彼此的文件,但是对文件的所有访问都是同步的,而且文件永远不会保持打开状态,所以
这不应导致任何访问问题。
 
如果应用程序名为`foo`,则脚本位置
be`world/scripts/foo.sc`,应用程序
特定的数据目录位于`world/scripts/foo.data/…`下,共享数据空间位于
`world/scripts/shared/…`。
 
默认的匿名应用程序只能通过`/script run`命令从共享空间保存/加载/读取文件。
 
如果不存在文件(用于读取,列出和删除操作),函数将返回`null`。返回`true`
对于成功的写入和删除,以及基于文件类型请求的数据,用于读取操作。它返回文件列表
用于文件夹列表。
 
资源`type`支持的值为：
*`nbt`-nbt标记
*`json`-json文件
*`text`-添加了自动换行的文本资源
*`raw`-没有隐含换行符的文本资源
*`folder`-仅用于`list files`-表示文件夹列表而不是文件
*`shared nbt`,`shared text`,`shared raw`,`shared folder`,`shared json`-上述内容的共享版本
 
NBT文件具有扩展名`.NBT`,存储一个NBT标记,并返回一个NBT类型值。JSON文件具有`.JSON`扩展名,存储
Scarpet数字,字符串,列表,映射和`null`值。其他任何内容都将保存为字符串(包括NBT)。
文本文件的扩展名为`.txt`,
存储多行文本并返回文件中所有行的列表。使用`write file`,可以选择多行
立即发送到文件。`raw`和`text`类型之间的唯一区别是在每种类型之后自动添加新行
记录到文件中。由于文件在每次写入后都会关闭,因此会将多行数据发送到
写有利于提高写速度。要发送多个数据包,请以平面形式或列表形式提供它们
第三个论点。
 
抛出：
-`nbt read error`：读取nbt文件失败。
-`json read error`：读取json文件失败。异常数据将包含有关问题的详细信息。
-`io\例外`：处理磁盘上与编码问题无关的数据时出现的所有其他错误
 
由于输入参数使用不当而导致的所有其他错误都应导致函数返回`null`,而不是异常
扔了。
 
<pre>
写入_文件(`foo`,`shared_text,[`one`,`two`]);
写入_文件(`foo`,`shared_text`,`three\n`,`four\n`);
写入_文件(`foo`,`shared_raw`,`five\n`,`six\n`);
 
read file(`foo`,`shared text`)=>[`一`,`二`,`三`,`四`,`五`,`六`]
</pre>
  
### `run(expr)`
 
从`expr`的字符串结果运行vanilla命令并返回三倍的成功计数,
截获的输出消息列表,以及命令导致失败时的错误消息。
成功的命令返回`null`作为错误。
 
<pre>
run(`fill 1 1 10 10 air`)->[123,[`Successfully filled 123 blocks`],null]//填充了123个块,此操作在可能的1000块卷中成功了123次
run(`give@s stone 4`)->[1,[`give 4[stone]to gnembon`],null]//此操作成功一次
run(`种子`)->[-170661413,[`种子：[403138449574382299]`],null]
run(`sed`)->[0,[],`sed<--[HERE]`]//命令错误
</pre>
 
###保存()`
 
执行自动保存,保存所有区块,播放器数据等。对于由于以下原因禁用自动保存的程序非常有用：
性能的原因和拯救世界只有在需求。
 
### `加载应用程序数据()`
 
注意：不推荐使用带参数的用法,因此`load app data(file)`和`load app data(file,shared?)`。
改用`read file`。
 
从world/scripts文件夹加载与应用程序关联的应用程序数据。无参数返回内存
管理和缓冲/限制NBT标签。使用文件名,从
只属于应用程序的脚本文件夹。如果`shared`为true,则文件位置不是独占的
应用程序,但位于共享的应用程序空间。
 
文件描述符可以包含字母,数字和文件夹分隔符：``/``。任何其他字符都会被剥离
保存/加载前的名称。空描述符无效。不要向描述符添加文件扩展名
 
函数返回包含文件内容的nbt值,如果文件丢失或出现问题,则返回`null`
检索数据。
 
默认的匿名应用程序只能通过`/script run`命令从共享数据位置保存/加载文件。
 
如果应用程序名为`foo`,则脚本位置
be`world/scripts/foo.sc`,系统管理的默认应用程序数据存储在`world/scripts/foo.data.nbt`,app
特定的数据目录位于`world/scripts/foo.data/bar/./baz.nbt`下,共享数据空间位于
`world/scripts/shared/bar/./baz.nbt`。
 
您可以使用应用程序数据将非常规信息与世界和其他脚本分开保存。
 
如果无法读取应用程序数据,则抛出`nbt read error`。
 
### `store app data(标记)`
 
注意：`store app data(tag,file)`和`store app data(tag,file,shared?)`用法已弃用。改用`write file`。
 
存储与world`/scripts`文件夹中的应用程序关联的应用程序数据。使用`file`参数保存
立即调用由`file`定义的特定文件(在应用程序空间或脚本中)
如果`shared`为true,则为shared space。如果没有`file`参数,则最多需要10分钟
输出文件的秒数
同步以防止此标记频繁更改时闪烁。它将在服务器关闭时同步。
 
如果文件保存成功,则返回`true`,否则返回`false`。
 
对独占应用程序数据和共享数据文件夹使用与`加载应用程序数据`相同的文件结构。
 
### `create\数据包(名称,数据)`
 
创建并加载自定义数据包。数据必须是表示文件结构和内容的映射
目标包的json文件。
 
如果具有此名称的包已存在或已加载,则返回`null`,表示未进行任何更改。
如果添加数据包失败,则返回`false`。
如果数据包的创建和加载成功,则返回`true`。加载数据包会导致
重新加载所有其他数据包(普通限制,与/datapack enable相同),但与`/reload`不同
命令,scarpet应用程序将不会通过使用`create datapack`添加数据包来重新加载。
 
目前,包中只支持json文件pack.mcmeta``文件将自动添加。
 
重新加载定义新维度的数据包在vanilla中没有实现。仅香草游戏加载
服务器启动时的维度信息`因此,create\ u datapack`直接替换了指定
数据包文件中的文件结构,并在新数据包上调用`/datapack enable`及其所有怪癖和副作用
(如不更改worldgen,重新加载所有其他数据包等)。要启用新添加的自定义维度,请调用更多
如果需要,在添加数据包后,进行实验性的`check hidden dimensions()`。
 
简介：
<pre>
脚本运行create datapack(`foo`,
{
`foo`->{`bar.json`->{
`c`->正确,
`d`->错误,
`e`->{`foo`->[1,2,3]},
`a`->`foobar`,
‘b’->5
} }
})
</pre>
 
自定义尺寸示例：
<pre>
脚本运行create datapack(`funky world`{
`data`->{`minecraft`->{`dimension`->{`custom ow.json`->{
`类型`->`minecraft:the_end`,
`发电机`->{
`生物群落来源`->{
`种子`->0,
`大型生物群落`->错误,
`类型`->`minecraft:vanilla_layered`
},
`种子`->0,
`设置`->`minecraft:nether`,
`类型`->`minecraft:noise`
} } } } }
});
检查_hidden_dimensions();=>[`时髦的世界`]
</pre>
 
战利品表示例：
<pre>
脚本运行create datapack(`silverfishes drop gravel`{
`data`->{`minecraft`->{`loot tables`->{`entities`->{`silverfish.json`->{
`类型`->`minecraft:entity`,
`池`->[
{
`转鼓`->{
`最小值`->0,
`最大值`->1
},
`条目`->[
{
`类型`->`minecraft:item`,
`名称`->`minecraft:gravel`
}
]
}
]
} } } } }
});
</pre>
 
配方示例：
<pre>
脚本运行create datapack(`craftable cobwebs`{
`data`->{`scarpet`->{`recipes`->{`cobweb.json`->{
`类型`->`工艺形状`,
`模式`->[
`SSS`,
`SSS`,
`SSS`
],
`键`->{
`S`->{
`项目`->`minecraft:string`
}
},
`结果`->{
`项目`->`minecraft:cobweb`,
`计数`->1
}
} } } }
});
</pre>
 
### `启用隐藏维度()`
 
函数读取当前数据包设置,检测这些数据包定义的尚未添加的新维度
添加到当前维度的列表中,以便可以立即使用和访问它们。不管天气如何
数据包已添加到游戏中,可以使用`create datapack()`或通过删除数据包文件并调用
`/对其启用数据包。返回已添加到进程中的有效维度名称/标识符的列表。
 
精细打印：功能应该是
被认为是实验性的。如果没有添加世界,`不应该有`(著名的最后一句话)任何副作用。已连接
客户端将看不到使用维度`/execute in<dim>`的命令的建议(普通客户端限制)
但是所有的命令都应该在
新的维度。已被数据包修改设置的现有世界将不会被重新加载或替换。
以这种方式添加的维度的可用性还没有得到充分的测试,但它似乎工作得很好。发电机设置
因为新的维度不会被添加到`level.dat`中,但它会在下次游戏重新启动时自动添加到那里
香草。可以说,使用这种方法时要谨慎,作者对由此造成的任何损失概不负责
错误地处理临时添加的维度,但是特性本身(自定义维度)对于Mojang来说显然是实验性的
他们自己,就这样。
 
### `tick time()`
 
返回服务器计时计数器。可用于每n个滴答声运行一次特定操作,或计算游戏时间。
 
### `世界时间()`
 
返回维度特定的记号计数器。
 
### `日\时(新\时?)`
 
返回当前白天时钟值。如果指定了`new time`,则设置一个新时钟
达到这个值。日间时钟在所有维度之间共享。
 
### `last tick times()`
 
返回一个100长的最近滴答时间数组,以毫秒为单位。清单上的第一项是最近的记号
如果在主勾号之外调用(通过计划任务或异步执行),则
列表可参考先前的勾选性能。在这种情况下,最后一个条目(勾选100)指的是最新的
滴答声。出于所有目的,应将`last tick times()：0`用作最后一次tick执行时间,但
个别的滴答声时间可能会有很大的不同,这些都需要采取与小颗粒的食物
平均值。
 
### `游戏时间(mstime?)`
 
使游戏运行一次。默认情况下,它运行它并将控制权返回给程序,但可以选择
接受预期的记号长度(毫秒),等待额外的剩余时间,然后将控件返回给程序。
你不能用它来永久改变游戏速度,但是设置
如果您可以访问
命令终端。
 
运行`game_tick()`作为在游戏tick本身中运行的代码的一部分通常是个坏主意,
除非你知道这意味着什么。触发`game_tick()`将导致当前(肩部)的tick暂停,然后运行内部tick,
然后运行肩勾的其余部分,这可能会导致在常规代码执行和游戏模拟代码之间出现工件。
如果你需要打破
将您的执行划分为多个块,您可以使用`schedule`将剩余的工作排队到下一个任务中,或者执行您的操作
定义事件处理程序` on  tick()`,但是如果您需要完全控制游戏循环并使用
`game_tick()`作为推进游戏进程的方法,这可能是最简单的方法,
并以`正确`的方式触发脚本(没有`正确`的方式,但通过命令行或服务器聊天是最`正确`的方式),
是最安全的方法。例如,从按钮触发的命令块或在实体中运行`game tick()`
实体勾选中触发的事件,技术上可能
使游戏运行并再次遇到该调用,导致堆栈溢出。谢天谢地,这在香草赛跑中没有发生
地毯,但可能发生与其他修改(修改)版本的游戏。
 
<pre>
loop(1000,game_tick())//尽可能快地运行游戏1000个tick
loop(1000,game_tick(100))//以两倍于1000 tick的速度运行游戏
</pre>
 
 
### `seed()`已弃用
 
返回当前世界种子。函数已弃用,请使用`system info(`world seed`)`insteads。
 
### `当前维度()`
 
返回脚本在其中运行的当前维度。
 
### `in\维度(smth,expr)`
 
使用不同的维度执行上下文计算表达式`expr``smth`可以是实体,
世界本地化块,因此不是`block(`stone`)``,也不是表示如下维度的字符串：
``幽冥`,`幽冥`,`终结`或`超世界`等。
 
如果找不到提供的维度,则抛出`未知的维度`。
 
### `view\距离()`
返回服务器的视距。
 
### `get\ mob\ U counts()`,`get\ mob\ U counts(类别)`1.16+
 
返回具有相应计数和容量的mob类别的映射(也称为mobcaps),或者仅返回一个元组
特定类别的计数和限制。如果一个类别不是因为任何原因而产生的,那么它可能不是
从`get mob counts()``返回,但无法检索到`get mob counts(category)``。返回计数是什么产卵
算法已经考虑了上次怪物繁殖的时间。
 
### `schedule(延迟,函数,参数…)`
 
安排用户定义的函数以指定的`delay`延迟时间运行。计划的函数在最后运行
它们将按预定的顺序运行。
 
如果您想安排模块中未定义的函数,请阅读上的提示
`将函数引用传递给应用程序的其他模块`部分。
 
### `statistic(播放器,类别,条目)`
 
在游戏中查询特定值的统计信息。类别包括：
 
*`已开采`：已开采的区块
*`crafted`:精心制作的项目
*`used`：使用的项目
*`breaked`：项目已损坏
*`picked\\ up`：拾取的项目
*`dropped`：删除的项目
*`被杀`：暴徒被杀
*`被杀`：暴徒被杀
*`custom`：各种随机统计信息
 
有关`entry`的选项,请查阅您的统计页面,或进行猜测。
 
如果统计选项不正确,或者播放器的历史记录中没有这些选项,则调用将返回`null`。
如果玩家遇到统计数据,或者为他创建的游戏为空,它将返回一个数字。
Scarpet不会影响统计数据的条目,即使它只是创建空条目。带有`null`响应
这可能意味着您的输入错误,或者统计数据的值实际上为 `0` 。
 
 
### `system_info()`,`system_info(属性)`
在不带任何参数的情况下调用时,获取系统属性的值或将所有信息作为映射返回。它可以用来
获取各种信息,大多数不改变,或只能通过低级别
系统调用。在所有情况下,这些仅以只读形式提供。
 
scarpet应用程序空间中的可用选项：
*`app name`—当前应用程序名,如果是默认应用程序,则为`null`
*`app list`-除默认命令行应用程序之外的所有已加载应用程序的列表
*`app scope`—全局变量和函数的作用域。可用选项为`player`和`global``
*`app player`-返回在其下运行app的播放器列表。对于`全局`应用程序,列表始终为空
 
相关世界相关财产
*`世界名称`-世界的名称
*`世界种子`-世界的数字种子
*`世界维度`-世界维度列表
*`world path`-世界保存文件夹的完整路径
*`world folder`—保存世界文件的直接文件夹的名称
*`world carpet rules`-以映射形式返回所有地毯规则(`rule`->`value`)。请注意,值总是作为字符串返回,因此不能直接进行布尔比较。包含扩展及其命名空间中的规则(`namespace:rule`->`值`)。以后可以使用`on rule changes(rule,newValue)`事件侦听规则更改。
*`world gamerules`-返回地图形式的所有游戏规则(`rule`->`value`)。与地毯规则一样,值以字符串形式返回,因此可以使用适当的值转换,使用`bool()`或`number()`将它们转换为其他值。Gamerules是只读的,目的是阻止应用程序程序员弄乱服务器管理员有意应用的设置。当一个数据包把你的游戏规则设置搞砸的时候,这不是很烦人吗?使用`run(`gamerule…`)`仍然可以更改它们。
*`世界繁殖点`-世界繁殖点
 
相关游戏相关属性
*`游戏难度`-游戏当前的难度：`和平`,`简单`,`正常`或`困难`
*`game hardcore`-布尔值,判断游戏是否处于硬核模式
*`game storage format`-世界保存文件的格式,可以是`McRegion`或`Anvil``
*`game default gamemode`-新玩家的默认游戏模式
*`game max players`-加入世界时允许的最大玩家数
*`游戏视图距离`-视图距离
*`game mod name`-基本mod的名称。期待`织物``
*`game version`-游戏的基本版本
*`game target`-目标发布版本
*`游戏主要目标`-主要发行目标。对于1.12.2,那就是12
*`game minor reease`-次要发布目标。对于1.12.2,应该是2
*`game protocol`-协议版本号
*`game pack version`-数据包版本号
*`game data version`-游戏的数据版本。返回一个整数,以便比较。
*`game stable`-表示它是生产版本还是快照
  
服务器相关属性
*`server motd`-加入时可见的服务器的motd
*`server ip`-托管游戏的ip地址
*`server whitelisted`-布尔值,指示是否只有白名单玩家才能访问服务器
*`服务器\白名单`-允许登录的玩家列表
*`server banked players`-禁止玩家名单
*`server banked ips`-禁止IP地址列表
*`server dev environment`—布尔值,指示此服务器是否处于开发环境中。
*`server_mods`-将所有加载的mod作为字符串映射到其版本的映射
 
系统相关属性
*`java max memory`—JVM允许访问的最大内存
*`java allocated memory`—JVM当前分配的内存
*`java used memory`—JVM当前使用的内存
*`java cpu count`-处理器数
*`java version`-java的版本
*`java bits`—表示java有多少位的数字,32或64
*`java system cpu load`—系统使用的当前cpu百分比
*`java process cpu load`—JVM使用的当前cpu百分比
 
Scarpet相关属性
*`scarpet version`-返回您的scarpet附带的地毯版本。
 
##NBT存储
 
### `nbt storage()`,`nbt storage(key)`,`nbt storage(key,nbt)`
显示或修改单个存储NBT标记。不带参数,返回当前NBT存储的列表。对于指定的`key`,返回与当前`key`关联的`nbt`,如果存储不存在,则返回`null`。使用指定的`key`和`nbt`设置一个新的`nbt`值,返回与`key`关联的上一个值。
注意：这个NBT存储与整个服务器的所有普通数据包和脚本共享,并且在重启和重新加载之间是持久的。您还可以使用vanilla`/data<get | modify | merge>storage<key>..`命令访问此NBT存储。
# `/script run`命令
 
输入命令的主要方式。命令在执行玩家的上下文,位置和维度中执行,
命令块等。。。该命令接收4个变量,`x`,`y`,`z`和`p`表示位置和位置
命令的执行实体。键入代码时,您将收到选项卡完成建议
函数和全局变量。建议在…中使用`/execute。。。在。。。作为。。。运行脚本运行…`或类似命令,
模拟在不同范围内运行命令。
 
# `/script load/unload<app>(全局?`,`/script in<app>`命令
 
`load/unload`命令允许以非常方便的方式编写代码,并将其提供给游戏和应用程序
不需要使用commandblocks就可以与您的世界一起分发。把你的宠物代码放在
`/脚本`你的世界文件的文件夹,并确保它以`.sc`扩展名结束。在单人游戏中,你可以
还可以将脚本保存在`.minecraft/config/carpet/scripts`中,以使它们在任何世界都可用。
 
编辑代码的好处是,你不仅可以使用普通的编辑而不需要换行,
但您也可以在代码中使用注释。
 
注释是任何以双斜杠开始,并一直延续到行尾的内容：
 
<pre>
foo = 1;
//This is a comment
bar = 2;
// This never worked, so I commented it out
// baz = foo()
</pre>
 
### `/script load/unload <app> (?global)`
 
加载操作将从磁盘加载脚本代码并立即执行。你可能会用它来加载
以后使用的一些存储过程。要重新加载模块,只需再次键入`/script load`。重新加载删除
模块稍后添加的所有当前全局状态(全局和函数)。重新加载所有应用
所有游戏资源,使用vanilla`/reload`命令。
 
 
 
加载的应用程序能够存储和加载外部文件,尤其是它们的持久标记状态。为了这个
选中`加载应用程序数据`和`存储应用程序数据`功能。
 
 
 
卸载应用程序只会屏蔽他们的命令树,而不会删除它。这与没有该命令的效果相同
除了加载同名的不同应用程序外,这可能会导致命令重新出现。
要完全删除命令,请使用`/reload`。
 
 
 
### `/script in <app> ...`
 
允许在特定应用程序中运行普通/脚本命令,如`run,invoke,…,globals`等。。。
 
# `/script invoke/invokepoint/invokearea`,`/script globals`命令
 
`invoke`命令族提供了调用存储过程(即已被调用的函数)的方便方法
以前由任何正在运行的脚本定义。要查看当前存储过程集,
运行`/script globals`(或`/script globals all`以显示所有函数,甚至隐藏的函数),定义一个新的存储
程序,只需运行`/script run函数(a,b)->(…)`命令你的程序一次,忘记一次
过程中,使用`undef`函数：`/script run undef(`function`)`
 
### `/script invoke <fun> <args?> ...`
 
相当于运行`/script run fun(args,…)`,但是您可以获得
命令名,以及运行这些命令所需的较低权限级别(因为播放器不能运行任何自定义命令)
代码(在本例中,仅此代码以前由操作员执行)。将检查参数的有效性,
并且只能将简单值作为参数(字符串,数字或`null`值)传递。使用引号包括
参数字符串中的空格。
 
命令将用必需的参数(count)检查提供的参数,如果不够或太多,则失败
提供了参数。建议定义函数的运算符使用描述性参数名称,因为
将对调用程序可见,并形成理解每个参数的作用的基础。
 
`invoke`family of commands tab将完成任何不以`'__'`开头的存储函数,它仍将
允许运行以`'__'`开头的过程,但不建议它们,并禁止执行任何隐藏的存储过程,
所以那些以`'__'`开头的。以防操作员为了方便而需要使用子例程,并且不想暴露
如果将它们发送给`invoke`调用者,则可以使用此机制。
 
<pre>
/script run example_function(const, phrase, price) -> print(const+' '+phrase+' '+price)
/script invoke example_function pi costs 5
</pre>
 
### `/script invokepoint<fun><coords x y z><args?>`
 
它与`invoke`等价,只是它假定前三个参数是坐标,并提供
`坐标`选项卡完成,为方便起见,带有`查看…`机制。需要所有其他参数
最后
 
### `/script invokearea<fun><coords x y z><coords x y z><args?>`
 
它与`invoke`等价,只是它假定前三个参数是一组对应项,
然后是第二组坐标,提供制表符补全,为方便起见,带有`查看…`机制,
后跟任何其他必需的参数
 
# `/script scan`,`/script fill`和`/script outline`命令
 
这些命令可用于计算块区域上的表达式。他们都需要指定
分析区域的原点(用作参考(0,0,0)和要分析区域的两个角点。如果你
如果希望脚本块坐标引用实际世界坐标,请使用原点 `0` ,或者如果
没关系,复制其中一个角的坐标是最简单的方法。
 
与raw`/script run`不同,这些命令受vanilla fill/clone命令32k块的限制,它可以
使用地毯模组自己的`/carpet fillLimit`命令进行更改。
 
### `/script scan origin<x y z> corner<x y z> corner<x y z> expr`
 
计算区域中每个点的表达式并返回成功数(结果为正)。自从
命令本身不会影响区域,效果会有副作用。
 
### `/script fill origin<x y z> corner<x y z> corner<x y z> "expr" <block> (? replace <replacement>)`
 
可以将其视为常规填充命令,它根据命令的结果是否成功来设置块。
请注意,表达式用引号括起来。谢天谢地,`scarpet`中的字符串常量使用单引号。可以用来
填充复杂的几何形状。
 
### `/script outline origin<x y z> corner<x y z> corner<x y z> "expr" <block> (? replace <replacement>)`
 
与`fill`命令类似,它为区域中的每个块计算表达式,但在本例中是设置块
其中条件为真,任何相邻区块的评估均为负。这样可以创建曲面
区域,例如球体,无需使用各种舍入模式和技巧。
 
以下是绘制半径为32个块的球体的七种方法的示例,以0 100 0为中心：
 
<pre>
/script outline 0 100 0 -40 60 -40 40 140 40 "x*x+y*y+z*z <  32*32" white_stained_glass replace air
/script outline 0 100 0 -40 60 -40 40 140 40 "x*x+y*y+z*z <= 32*32" white_stained_glass replace air
/script outline 0 100 0 -40 60 -40 40 140 40 "x*x+y*y+z*z <  32.5*32.5" white_stained_glass replace air
/script fill    0 100 0 -40 60 -40 40 140 40 "floor(sqrt(x*x+y*y+z*z)) == 32" white_stained_glass replace air
/script fill    0 100 0 -40 60 -40 40 140 40 "round(sqrt(x*x+y*y+z*z)) == 32" white_stained_glass replace air
/script fill    0 100 0 -40 60 -40 40 140 40 "ceil(sqrt(x*x+y*y+z*z)) == 32" white_stained_glass replace air
/draw sphere 0 100 0 32 white_stained_glass replace air // fluffy ball round(sqrt(x*x+y*y+z*z)-rand(abs(y)))==32
</pre>
 
最后一种方法是world edit正在使用的方法(地毯模型的一部分)。原来,轮廓法
对于`32.5`半径,`round`函数的填充方法和draw命令是等效的
 
# `script stop/script resume`命令
 
`/script stop`允许停止执行当前正在运行的调用`game tick()`函数的任何脚本,该函数
允许游戏循环重新控制游戏并处理其他命令。这也将确保
当前和将来的程序将停止执行。将阻止执行所有程序
直到调用`/script resume`命令。
 
让我们看看下面的例子。这是一个以递归方式计算斐波那契数的程序：
 
<pre>
fib(n) -> if(n<3, 1, fib(n-1)+fib(n-2) ); fib(8)
</pre>
 
这是一种非常糟糕的方法,因为我们需要计算的计算需求越多
与`n`成指数增长。做fib(24)需要50毫秒多一点,所以超过一个刻度,但是大约
一分钟做fib(40)。调用fib(40)不仅会冻结游戏,而且你不能中断
它的执行。我们可以修改脚本如下
 
<pre>
fib(n) -> ( game_tick(50); if(n<3, 1, fib(n-1)+fib(n-2) ) ); fib(40)
</pre>
 
但这永远不会结束,因为这样的调用将在`~2^40`滴答声之后结束。为了让我们的计算更有效率,
但由于能够响应用户交互,其他命令以及中断执行,我们可以执行以下操作：
 
<pre>
fib(n) -> ( if(n==23, game_tick(50) ); if(n<3, 1, fib(n-1)+fib(n-2) ) ); fib(40)
</pre>
 
这会将fib(40)的计算速度从一分钟减慢到两分钟,但允许游戏继续运行
并对命令做出反应,利用每个记号的一半来推进计算。显然取决于
问题,以及可用的硬件,某些事情可能需要或多或少的时间来执行,因此使用
调用`gametick`应该在每种情况下分别平衡

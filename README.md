# AndroidTvRectAnim
作者： 夏至，欢迎转载，但请保留这段申明，谢谢 
http://blog.csdn.net/u011418943/article/details/76687529

在 Android Tv 或者其他需要使用 遥控器来操作的设备，有一个好看的游标动画，除了能提高用户体验外，还能让你的作品看起来更加智能化，当然，github 上已经有开源出来了，如下：
https://git.oschina.net/kumei/AndroidTVWidget
效果很不错，但结构比较大，移植和理解起来也比较麻烦，那能不能简单点呢？

 答案是肯定的，先看效果：
![这里写图片描述](http://img.blog.csdn.net/20170804162039077?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMTQxODk0Mw==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

如果在我们的设备中，使用这种效果，那么看起来会很舒服，本着都开源的原则，这里我也把我的代码共享出来。让 TV 的小伙伴少走一些弯路。工程连接在末尾。

  原理其实很简单，当一个控件在获取到焦点的时候，我**们就把这张 .9图设置当作它的背景，当移动到另一个控件的时候，这个背景图就要跟着移动了；**
那么边框怎么自动变化呢？

首先，获取现在控件的坐标值 **(left，top,right,bottom)**，减去上一次的控件的坐标值 **(left，top,right,bottom)**,这个过程用属性动画来，那么光标就可以从这个控件移动到另一个控件了，还能自由伸缩，这就是游标动画的核心所在，其他就没啥好写的了。

只要理解了思路，其他的就基本没问题了，整个**过程写起来撑死了100行**。

核心代码：

```
int translateLeft = torect.left - oldrect.left - mRectOffsertWidth; //左焦点
            int translateTop = torect.top - oldrect.top - mRectOffsertWidth;  //右焦点
            //当两者之间有大小之分，才去改变，减少绘制
            if (currentView.getWidth() - this.getWidth() != 0) {
                //移动时改变边框大小，让效果看起来更加自然
                layoutSizeChangeWidthAnim(this.getWidth(), currentView.getWidth() + 2 * mRectOffsertWidth);
                layoutSizeChangeHeightAnim(this.getHeight(), currentView.getHeight() + 2 * mRectOffsertWidth);
            }
            //移动函数
            flyRectAnim(translateLeft, translateTop);
```
上面的移动动画，分成两步，一部分是不需要太多`这里写代码片`变化的坐标 left，top，所以用 flyRectAnim 来写，如下：

```
 /**
     * 移动函数
     * @param left
     * @param top
     */
    private void flyRectAnim(int left,int top){
        //非常简单，我们只要提供left 和 top 即可
        animate().translationX(left).translationY(top).setDuration(mAnimationTime).
                setInterpolator(new LinearInterpolator()).start();
    }
```
直接移动过去即可，而 right 和 bottom ，为了有一种缓慢变形的感觉，我们需要重写一些，如下：

```
/**
     * 调整移动时，边框宽度的变化
     * @param oldwidht
     * @param curwidht
     */
    private void layoutSizeChangeWidthAnim(int oldwidht,int curwidht){
        ValueAnimator animator = ValueAnimator.ofFloat(oldwidht,curwidht);
        animator.setDuration(mAnimationTime - mAnimationTimeWH);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(final ValueAnimator animation) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        float width = (float) Float.parseFloat( animation.getAnimatedValue().toString());
                        ViewGroup.LayoutParams params = mView.getLayoutParams();
                        params.width = (int) width;
                        mView.setLayoutParams(params);
                    }
                });
            }
        });
        animator.start();
    }
```
非常简单，就是一个 ValuteAnimator 属性动画；这样就可以了！！

什么？没了？

是的，没了，没多难理解，直接撸一遍就可以了。整个动画有用部分，就是移动控件的坐标，简单到发指。
工程连接如下，喜欢记得start 或fork啊

https://github.com/LillteZheng/AndroidTvRectAnim.git

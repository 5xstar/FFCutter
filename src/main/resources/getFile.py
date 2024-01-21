# Comments language: zh_cn
# encoding: UTF-8

# 需要前置 python
# 使用该工具可以帮助java使用系统自带的文件资源管理器选择文件
# 该工具允许任何用途，不进行版权限制。
# 用法请看以下注释

try:
    from tkinter import filedialog
    import sys, ctypes, json

    # 解决Windows系统屏幕缩放导致窗口模糊的问题
    if 'win' in sys.platform:
        ctypes.windll.shcore.SetProcessDpiAwareness(1)

    # 默认参数
    InputValues={
        'title':'openFile',
        'fileTypes':[['All files','*']],# 仅模式0与1有效
        'mode':0, # 输入模式代号 0选择单个文件 1选择多个文件 2选择文件夹 3导出文件
        'exportName':'export.txt', # 仅模式3有效
        'openPath':'' # 选择器弹出时，打开的文件夹路径（留空则打开上次的目录）
    }

    # 识别第2个启动参数，并覆盖默认参数字典
    # 启动命令示范（双引号的字符串里必须用单引号）：
    #   python getFile.py "{'title':'选择文本文件','mode':0,'fileTypes':[['文本文件','*.txt'],['所有文件','*']]}"
    try:
        InputValues.update(eval(sys.argv[1]))
    except: pass

    mode= int(InputValues['mode'])
    # 调用者捕捉命令行输出即可
    if mode== 0: #单个文件
        print(
            filedialog.askopenfilename(
                title= InputValues['title'], 
                filetypes= InputValues['fileTypes'],
                initialdir= InputValues['openPath']
            )
        )
    elif mode== 1: #多个文件（输出通用列表 ["filename","filename"]）
        print(
            '['+(
                ','.join(
                    json.dumps(i) for i in filedialog.askopenfilenames(
                        title= InputValues['title'],
                        filetypes= InputValues['fileTypes'],
                        initialdir= InputValues['openPath']
                    )
                )
            )+']'
        )
    elif mode== 2: #文件夹
        print(
            filedialog.askdirectory(
                title= InputValues['title'],
                initialdir= InputValues['openPath']
            )
        )
    elif mode== 3: #导出文件
        print(
            filedialog.asksaveasfilename(
                title= InputValues['title'],
                initialfile= InputValues['exportName'],
                filetypes= InputValues['fileTypes'],
                initialdir= InputValues['openPath']
            )
        )
    else:
        print(">Can't find mode",mode,'from input mode',InputValues['mode'],'!')

except Exception as err:
    print(">"+err)
    #若出现错误，则输出开头为大于号的内容
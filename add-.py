"""
writen by shenjackyuanjie
mail:   3695888@qq.com
github: @shenjackyuanjie
gitee:  @shenjackyuanjie
"""
import pprint

done = []
with open('test.md', 'r', encoding='utf-8') as file:
    for line in file.readlines():
        word = 0
        if line[0] != " ":
            new_line = "%s%s" % ('- ', line)
            done.append(new_line)
            continue
        # line[0] == " ":
        while True:
            word += 4
            if word > len(line):
                done.append(line)
                break
            elif line[word] != " ":
                new_line = "%s%s%s" % (line[:word], "- ", line[word:])
                done.append(new_line)
                break
            # line[word] == " "
            continue
pprint.pprint(done)
with open('test_test.md', 'w+', encoding='utf-8') as file:
    file.writelines(done)

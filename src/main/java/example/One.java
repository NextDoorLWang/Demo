package example;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class One {
    String message = "foo";

    public One() {
    }

    // 线程数
    private int threadCount = 6;

    // 锁
    CountDownLatch countDownLatch;

    // 选定的标准
    private String[] standards;

    public int getThreadCount() {
        return threadCount;
    }

    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }

    List<String> inputList = new ArrayList<String>();

    List<String[]> outputList = new ArrayList<String[]>();

    List<String> result = new ArrayList<String>();


    public String foo() {
        countDownLatch = new CountDownLatch(threadCount);
        readFile();
        subList();
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < outputList.size(); i++) {
            String[] strings = outputList.get(i);
            for (int j = 0; j < strings.length; j++) {
                result.add(strings[j]);
            }
        }

        writeFile();

        return message;
    }


    private void readFile() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("in.txt")))) {
            String line;
            while ((line = br.readLine()) != null) {
                inputList.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void writeFile() {
        System.out.println("Start to write file");
        try (PrintWriter pw = new PrintWriter(new FileWriter("out.txt"))) {
            for (int i = 0; i < result.size(); i++) {
                pw.println(result.get(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    //排序
    private void subList() {
        if (inputList.size() == 0) {
            return;
        }
        if (threadCount >= inputList.size()) {
            threadCount = inputList.size();
        }

        standards = new String[threadCount - 1];

        for (int i = 0; i < threadCount - 1; i++) {
            standards[i] = inputList.get(i);
        }
        //快速排序
        quickSort(standards);

        List<List<String>> subLists = new ArrayList<>(threadCount);


        for (int i = 0; i < threadCount; i++) {
            subLists.add(new ArrayList<>(10));
        }

        for (int i = 0; i < inputList.size(); i++) {
            boolean isPut = false;
            for (int j = 0; j < standards.length; j++) {

                if (inputList.get(i).compareTo(standards[j]) <= 0) {
                    subLists.get(j).add(inputList.get(i));
                    isPut = true;
                    break;
                }
            }
            if (!isPut)
                subLists.get(threadCount - 1).add(inputList.get(i));
        }

        for (int i = 0; i < threadCount; i++) {
            List<String> list = subLists.get(i);
            String[] strings = new String[list.size()];
            list.toArray(strings);
            outputList.add(strings);
        }

        for (int i = 0; i < threadCount; i++) {
            String[] strings = outputList.get(i);
            new Thread(() -> {
                quickSort(strings);
                countDownLatch.countDown();
            }).start();
        }
    }

    private void quickSort(String[] arrays) {
        subQuickSort(arrays, 0, arrays.length - 1);
    }

    private void subQuickSort(String[] arrays, int start, int end) {
        if (start >= end) {
            return;
        }
        int middleIndex = subQuickSortCore(arrays, start, end);
        subQuickSort(arrays, start, middleIndex - 1);
        subQuickSort(arrays, middleIndex + 1, end);
    }

    private int subQuickSortCore(String[] arrays, int start, int end) {
        String middleValue = arrays[this.SelectPivotMedianOfThree(arrays,start,end)];
        while (start < end) {
            while (arrays[end].compareTo(middleValue) >= 0 && start < end) {
                end--;
            }
            arrays[start] = arrays[end];
            while (arrays[start].compareTo(middleValue) <= 0 && start < end) {
                start++;
            }
            arrays[end] = arrays[start];
        }
        arrays[start] = middleValue;
        return start;
    }


    //选取合理中位数
    int SelectPivotMedianOfThree(String arr[], int low, int high) {
        int mid = low + ((high - low) >> 1);//计算数组中间的元素的下标
        String middle = "";
        //使用三数取中法选择枢轴
        if (arr[mid].compareTo(arr[high]) > 0)//目标: arr[mid] <= arr[high]
        {
            middle = arr[high];
            arr[high] = arr[mid];
            arr[mid] = middle;
        }
        if (arr[low].compareTo(arr[high]) > 0)//目标: arr[low] <= arr[high]
        {
            middle = arr[high];
            arr[high] = arr[low];
            arr[low] = middle;
        }
        if (arr[mid].compareTo(arr[low]) > 0) //目标: arr[low] >= arr[mid]
        {
            middle = arr[high];
            arr[high] = arr[mid];
            arr[mid] = middle;
        }
        //此时，arr[mid] <= arr[low] <= arr[high]
        return low;
        //low的位置上保存这三个位置中间的值
        //分割时可以直接使用low位置的元素作为枢轴，而不用改变分割函数了
    }



}
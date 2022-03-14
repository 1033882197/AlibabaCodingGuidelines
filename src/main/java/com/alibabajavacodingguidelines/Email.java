package com.alibabajavacodingguidelines;

import java.util.HashSet;

/**
 * @description: Test <br>
 * @date: 2022/3/11 9:55 <br>
 * @author: Rile <br>
 * @version: 1.0 <br>
 */
public class Email
{
    public String address;

    public Email(String address)
    {
        this.address = address;
    }

    public int hashCode()
    {
        int result = address.hashCode();
        return result;
    }

    public static void main(String[] args)
    {
        int[] a = new int[]{5, 6};
        HashSet set = new HashSet();
        Email email = new Email("huawei.com");
        set.add(email);
        email.address = "silong.com";
        System.out.println(set.contains(email)); //1
        set.remove(email);
        System.out.println(set.size());  //2
    }
}

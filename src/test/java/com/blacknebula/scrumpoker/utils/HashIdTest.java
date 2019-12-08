package com.blacknebula.scrumpoker.utils;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class HashIdTest {
    String salt_ = "this is my salt";
    HashId hashId_;

    @Before
    public void setUp() throws Exception {
        hashId_ = new HashId(salt_);
    }

    @Test
    public void itHasDefaultSalt() {
        assertEquals(new HashId().encrypt(1, 2, 3), "katKSA");
    }

    @Test
    public void itHasTheCorrectSalt() {
        assertEquals(hashId_.getSalt(), "this is my salt");
    }

    @Test
    public void itDefaultsToTheMinimumLength0() {
        assertEquals(hashId_.getMinHashLength(), 0);
    }

    @Test
    public void itEncryptsASingleNumber() {
        assertEquals(hashId_.encrypt(12345), "ryBo");
        assertEquals(hashId_.encrypt(1), "LX");
        assertEquals(hashId_.encrypt(22), "5B");
        assertEquals(hashId_.encrypt(333), "o49");
        assertEquals(hashId_.encrypt(9999), "GKnB");
    }

    @Test
    public void itEncryptsAListOfNumbers() {
        assertEquals(hashId_.encrypt(683, 94108, 123, 5), "zBphL54nuMyu5");
        assertEquals(hashId_.encrypt(1, 2, 3), "eGtrS8");
        assertEquals(hashId_.encrypt(2, 4, 6), "9Kh7fz");
        assertEquals(hashId_.encrypt(99, 25), "dAECX");
    }

    @Test
    public void itReturnsAnEmptyStringIfNoNumbers() {
        assertEquals(hashId_.encrypt(), "");
    }

    @Test
    public void itCanEncryptToAMinimumLength() {
        HashId h = new HashId(salt_, 8);
        assertEquals(h.encrypt(1), "b9iLXiAa");
    }

    @Test
    public void itDoesNotProduceRepeatingPatternsForIdenticalNumbers() {
        assertEquals(hashId_.encrypt(5, 5, 5, 5), "GLh5SMs9");
    }

    @Test
    public void itDoesNotProduceRepeatingPatternsForIncrementedNumbers() {
        assertEquals(hashId_.encrypt(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), "zEUzfySGIpuyhpF6HaC7");
    }

    @Test
    public void itDoesNotProduceSimilaritiesBetweenIncrementingNumberHashes() {
        assertEquals(hashId_.encrypt(1), "LX");
        assertEquals(hashId_.encrypt(2), "ed");
        assertEquals(hashId_.encrypt(3), "o9");
        assertEquals(hashId_.encrypt(4), "4n");
        assertEquals(hashId_.encrypt(5), "a5");
    }

    @Test
    public void itDecryptsAnEncryptedNumber() {
        assertArrayEquals(hashId_.decrypt("ryBo"), new long[]{12345});
        assertArrayEquals(hashId_.decrypt("qkpA"), new long[]{1337});
        assertArrayEquals(hashId_.decrypt("6aX"), new long[]{808});
        assertArrayEquals(hashId_.decrypt("gz9"), new long[]{303});
    }

    @Test
    public void itDecryptsAListOfEncryptedNumbers() {
        assertArrayEquals(hashId_.decrypt("zBphL54nuMyu5"), new long[]{683, 94108, 123, 5});
        assertArrayEquals(hashId_.decrypt("kEFy"), new long[]{1, 2});
        assertArrayEquals(hashId_.decrypt("Aztn"), new long[]{6, 5});
    }

    @Test
    public void itDoesNotDecryptWithADifferentSalt() {
        HashId peppers = new HashId("this is my pepper");
        assertArrayEquals(hashId_.decrypt("ryBo"), new long[]{12345});
        assertArrayEquals(peppers.decrypt("ryBo"), new long[0]);
    }

    @Test
    public void itCanDecryptFromAHashWithAMinimumLength() {
        HashId h = new HashId(salt_, 8);
        assertArrayEquals(h.decrypt("b9iLXiAa"), new long[]{1});
    }

    @Test(expected = IllegalArgumentException.class)
    public void itRaisesAnArgumentNullExceptionWhenAlphabetIsNull() {
        new HashId("", 0, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void itRaisesAnArgumentNullExceptionIfAlphabetContainsLessThan4UniqueCharacters() {
        new HashId("", 0, "aadsss");
    }

    @Test
    public void itCanEncryptWithASwappedCustom() {
        HashId hashIds = new HashId("this is my salt", 0, "abcd");
        assertEquals(hashIds.encrypt(1, 2, 3, 4, 5), "adcdacddcdaacdad");
    }
}
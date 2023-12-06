package com.github.remilamoureux.seg_project_group72;

import org.junit.Test;

import static org.junit.Assert.*;

import com.github.remilamoureux.seg_project_group72.data.Ejoin;
import com.github.remilamoureux.seg_project_group72.data.Event;
import com.github.remilamoureux.seg_project_group72.data.EventType;
import com.github.remilamoureux.seg_project_group72.data.Listable;

import java.util.ArrayList;
import java.util.List;

public class UnitTests {
    @Test
    public void test1() {
        Ejoin e = new Ejoin("name", "info");
        assertEquals(e.getItemName(), "name");
    }
    @Test
    public void test2() {
        Ejoin e = new Ejoin("name", "info");
        assertEquals(e.getItemDesc(), "info");
    }
    @Test
    public void test3() {
        EventType type = new EventType("name", "desc", "id");
        List<Ejoin> participants = new ArrayList<>();
        Event e = new Event("ename", "edesc", type, "eid", participants);
        assertEquals(e.getName(), "ename");
    }
    @Test
    public void test4() {
        EventType type = new EventType("name", "desc", "id");
        List<Ejoin> participants = new ArrayList<>();
        Event e = new Event("ename", "edesc", type, "eid", participants);
        assertEquals(e.getTypeName(), "name");
    }
    @Test
    public void test5() {
        EventType type = new EventType("name", "desc", "id");
        List<Ejoin> participants = new ArrayList<>();
        Event e = new Event("ename", "edesc", type, "eid", participants);
        assertEquals(e.getDescription(), "edesc");
    }
    @Test
    public void test6() {
        EventType type = new EventType("name", "desc", "id");
        List<Ejoin> participants = new ArrayList<>();
        Event e = new Event("ename", "edesc", type, "eid", participants);
        assertEquals(e.getTypeDescription(), "desc");
    }
    @Test
    public void test7() {
        EventType type = new EventType("name", "desc", "id");
        List<Ejoin> participants = new ArrayList<>();
        Event e = new Event("ename", "edesc", type, "eid", participants);
        assertEquals(e.getTypeName(), e.getType().getName());
    }
    @Test
    public void test8() {
        EventType type = new EventType("name", "desc", "id");
        List<Ejoin> participants = new ArrayList<>();
        Event e = new Event("ename", "edesc", type, "eid", participants);
        assertEquals(e.getTypeDescription(), e.getType().getDescription());
    }
    @Test
    public void test9() {
        EventType type = new EventType("name", "desc", "id");
        List<Ejoin> participants = new ArrayList<>();
        Event e = new Event("ename", "edesc", type, "eid", participants);
        assertEquals(participants, e.getParticipants());
    }
    @Test
    public void test10() {
        EventType type = new EventType("name", "desc", "id");
        List<Ejoin> participants = new ArrayList<>();
        Event e = new Event("ename", "edesc", type, "eid", participants);
        participants.add(new Ejoin("la", "lo"));
        assertEquals(participants, e.getParticipants());
    }
}
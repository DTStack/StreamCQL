/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.huawei.streaming.window.group;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.huawei.streaming.event.IEvent;
import com.huawei.streaming.event.IEventType;
import com.huawei.streaming.event.TupleEvent;
import com.huawei.streaming.expression.IExpression;
import com.huawei.streaming.expression.PropertyValueExpression;
import com.huawei.streaming.support.SupportConst;
import com.huawei.streaming.support.SupportEventMng;
import com.huawei.streaming.support.SupportGroupView;
import com.huawei.streaming.support.SupportGroupViewDataCheck;
import com.huawei.streaming.view.MergeView;

/**
 * 
 * <GroupLengthSlideWindowTest>
 * <功能详细描述>
 * 
 */
public class GroupLengthSlideWindowTest
{
    private IExpression[] exprs = null;
    
    private GroupLengthSlideWindow myView = null;
    
    private MergeView mergeView = null;
    
    private SupportGroupView childView = null;
    
    private SupportEventMng mng = null;
    
    private IEventType eventType = null;
    
    /**
     * <setup>
     */
    @Before
    public void setUp()
        throws Exception
    {
        exprs = new IExpression[] {new PropertyValueExpression("a", Integer.class)};
        myView = new GroupLengthSlideWindow(exprs, SupportConst.I_FIVE);
        mergeView = new MergeView();
        childView = new SupportGroupView(exprs);
        myView.addView(mergeView);
        mergeView.addView(childView);
        mng = new SupportEventMng();
        eventType = mng.getInput();
    }
    
    /**
     * <cleanup>
     */
    @After
    public void tearDown()
        throws Exception
    {
        exprs = null;
        
        myView.removeAllViews();
        mergeView.removeAllViews();
        myView = null;
        mergeView = null;
        childView = null;
        
        mng = null;
    }
    
    /**
     * <测试事件处理>
     * <功能详细描述>
     */
    @Test
    public void testProcessGroupedEvent()
    {
        Map<String, Object> values = new HashMap<String, Object>();
        values.put("a", 1);
        values.put("b", 1);
        values.put("c", "c1");
        
        IEvent event1 = new TupleEvent("stream", eventType, values);
        
        myView.processGroupedEvent(mergeView, null, 1, event1);
        SupportGroupViewDataCheck.checkOldData(1, childView, null);
        SupportGroupViewDataCheck.checkNewData(1, childView, new IEvent[] {event1});
        
        values = new HashMap<String, Object>();
        values.put("a", 1);
        values.put("b", 1);
        values.put("c", "c12");
        
        IEvent event12 = new TupleEvent("stream", eventType, values);
        
        myView.processGroupedEvent(mergeView, null, 1, event12);
        SupportGroupViewDataCheck.checkOldData(1, childView, null);
        SupportGroupViewDataCheck.checkNewData(1, childView, new IEvent[] {event12});
        
        values = new HashMap<String, Object>();
        values.put("a", 1);
        values.put("b", 1);
        values.put("c", "c13");
        
        IEvent event13 = new TupleEvent("stream", eventType, values);
        
        myView.processGroupedEvent(mergeView, null, 1, event13);
        SupportGroupViewDataCheck.checkOldData(1, childView, null);
        SupportGroupViewDataCheck.checkNewData(1, childView, new IEvent[] {event13});
        
        values = new HashMap<String, Object>();
        values.put("a", 1);
        values.put("b", 1);
        values.put("c", "c14");
        
        IEvent event14 = new TupleEvent("stream", eventType, values);
        
        myView.processGroupedEvent(mergeView, null, 1, event14);
        SupportGroupViewDataCheck.checkOldData(1, childView, null);
        SupportGroupViewDataCheck.checkNewData(1, childView, new IEvent[] {event14});
        
        values = new HashMap<String, Object>();
        values.put("a", 1);
        values.put("b", 1);
        values.put("c", "c15");
        
        IEvent event15 = new TupleEvent("stream", eventType, values);
        
        myView.processGroupedEvent(mergeView, null, 1, event15);
        SupportGroupViewDataCheck.checkOldData(1, childView, null);
        SupportGroupViewDataCheck.checkNewData(1, childView, new IEvent[] {event15});
        
        values = new HashMap<String, Object>();
        values.put("a", 1);
        values.put("b", 1);
        values.put("c", "c16");
        
        IEvent event16 = new TupleEvent("stream", eventType, values);
        
        myView.processGroupedEvent(mergeView, null, 1, event16);
        SupportGroupViewDataCheck.checkOldData(1, childView, new IEvent[] {event1});
        SupportGroupViewDataCheck.checkNewData(1, childView, new IEvent[] {event16});
    }
    
    /**
     * <测试构造函数>
     * <功能详细描述>
     */
    @Test
    public void testGroupLengthSlideWindow()
    {
        try
        {
            myView = new GroupLengthSlideWindow(null, SupportConst.I_FIVE);
            fail();
        }
        catch (IllegalArgumentException e)
        {
            assertTrue(true);
        }
        
        try
        {
            myView = new GroupLengthSlideWindow(new IExpression[] {}, SupportConst.I_FIVE);
            fail();
        }
        catch (IllegalArgumentException e)
        {
            assertTrue(true);
        }
        
        try
        {
            myView = new GroupLengthSlideWindow(new IExpression[] {}, SupportConst.I_ZERO);
            fail();
        }
        catch (IllegalArgumentException e)
        {
            assertTrue(true);
        }
        
        try
        {
            myView =
                new GroupLengthSlideWindow(new IExpression[] {new PropertyValueExpression("a", Integer.class)},
                    SupportConst.I_ZERO);
            fail();
        }
        catch (IllegalArgumentException e)
        {
            assertTrue(true);
        }
    }
    
    /**
     * <测试返回窗口长度>
     * <功能详细描述>
     */
    @Test
    public void testGetKeepLength()
    {
        assertEquals(SupportConst.I_FIVE, myView.getKeepLength());
    }
    
    /**
     * <测试窗口更新>
     * <功能详细描述>
     */
    @Test
    public void testUpdate()
    {
        Map<String, Object> values = new HashMap<String, Object>();
        values.put("a", 1);
        values.put("b", 1);
        values.put("c", "c1");
        
        IEvent event1 = new TupleEvent("stream", eventType, values);
        
        myView.update(new IEvent[] {event1}, null);
        
        SupportGroupViewDataCheck.checkOldData(1, childView, null);
        SupportGroupViewDataCheck.checkNewData(1, childView, new IEvent[] {event1});
        
        SupportGroupViewDataCheck.checkOldData(SupportConst.I_TWO, childView, null);
        SupportGroupViewDataCheck.checkNewData(SupportConst.I_TWO, childView, null);
        
        values = new HashMap<String, Object>();
        values.put("a", 1);
        values.put("b", 1);
        values.put("c", "c12");
        
        IEvent event12 = new TupleEvent("stream", eventType, values);
        
        myView.update(new IEvent[] {event12}, null);
        
        SupportGroupViewDataCheck.checkOldData(1, childView, null);
        SupportGroupViewDataCheck.checkNewData(1, childView, new IEvent[] {event12});
        
        values = new HashMap<String, Object>();
        values.put("a", 1);
        values.put("b", 1);
        values.put("c", "c13");
        
        IEvent event13 = new TupleEvent("stream", eventType, values);
        
        myView.update(new IEvent[] {event13}, null);
        
        SupportGroupViewDataCheck.checkOldData(1, childView, null);
        SupportGroupViewDataCheck.checkNewData(1, childView, new IEvent[] {event13});
        
        values = new HashMap<String, Object>();
        values.put("a", 1);
        values.put("b", 1);
        values.put("c", "c14");
        
        IEvent event14 = new TupleEvent("stream", eventType, values);
        
        myView.update(new IEvent[] {event14}, null);
        
        SupportGroupViewDataCheck.checkOldData(1, childView, null);
        SupportGroupViewDataCheck.checkNewData(1, childView, new IEvent[] {event14});
        
        values = new HashMap<String, Object>();
        values.put("a", SupportConst.I_TWO);
        values.put("b", SupportConst.I_TWO);
        values.put("c", "c2");
        
        IEvent event2 = new TupleEvent("stream", eventType, values);
        
        myView.update(new IEvent[] {event2}, null);
        
        values = new HashMap<String, Object>();
        values.put("a", 1);
        values.put("b", 1);
        values.put("c", "c15");
        
        IEvent event15 = new TupleEvent("stream", eventType, values);
        
        myView.update(new IEvent[] {event15}, null);
        SupportGroupViewDataCheck.checkOldData(1, childView, null);
        SupportGroupViewDataCheck.checkNewData(1, childView, new IEvent[] {event15});
        
        values = new HashMap<String, Object>();
        values.put("a", 1);
        values.put("b", 1);
        values.put("c", "c16");
        
        IEvent event16 = new TupleEvent("stream", eventType, values);
        
        myView.update(new IEvent[] {event16}, null);
        
        SupportGroupViewDataCheck.checkOldData(1, childView, new IEvent[] {event1});
        SupportGroupViewDataCheck.checkNewData(1, childView, new IEvent[] {event16});
        
        SupportGroupViewDataCheck.checkOldData(SupportConst.I_TWO, childView, null);
        SupportGroupViewDataCheck.checkNewData(SupportConst.I_TWO, childView, new IEvent[] {event2});
        
        try
        {
            myView.update(new IEvent[] {event1, event12}, null);
            fail();
        }
        catch (RuntimeException e)
        {
            assertTrue(true);
        }
        
        try
        {
            myView.update(new IEvent[] {event1}, new IEvent[] {event12});
            fail();
        }
        catch (RuntimeException e)
        {
            assertTrue(true);
        }
        
        try
        {
            myView.update(null, new IEvent[] {event12, event13});
            fail();
        }
        catch (RuntimeException e)
        {
            assertTrue(true);
        }
    }
    
}

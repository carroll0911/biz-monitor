package com.carroll.biz.monitor.analyzer.service;

import com.alibaba.fastjson.JSON;
import com.carroll.biz.monitor.analyzer.job.RabbitMqMonitorJob;
import com.carroll.biz.monitor.analyzer.model.BaseServiceMonitorItem;
import com.carroll.biz.monitor.analyzer.utils.Base64Util;
import com.carroll.utils.OkHttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.Method;
import java.util.Map;

import static org.mockito.Matchers.eq;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * RabbitMqMonitorJob Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>���� 25, 2018</pre>
 */
@RunWith(PowerMockRunner.class)
@Slf4j
@PrepareForTest(OkHttpUtil.class)
public class RabbitMqMonitorJobTest {


    /**
     * Method: mqSumInfo(BaseServiceMonitorItem item)
     */
    @Test
    public void testMqSumInfo() throws Exception {
        BaseServiceMonitorItem item = new BaseServiceMonitorItem();
        item.setTag("MQ");
        item.setUri("http://172.20.68.7:15672/api/queues");
        String username = "c3";
        String password = "c3123";
        item.setAuthorization("Basic " + Base64Util.encode((username + ":" + password).getBytes()));
        PowerMockito.mockStatic(OkHttpUtil.class);
        String str = "[{\"memory\":22368,\"message_stats\":{\"disk_reads\":0,\"disk_reads_details\":{\"rate\":0.0},\"disk_writes\":224,\"disk_writes_details\":{\"rate\":0.0},\"deliver\":224,\"deliver_details\":{\"rate\":0.0},\"deliver_no_ack\":0,\"deliver_no_ack_details\":{\"rate\":0.0},\"get\":0,\"get_details\":{\"rate\":0.0},\"get_no_ack\":0,\"get_no_ack_details\":{\"rate\":0.0},\"publish\":224,\"publish_details\":{\"rate\":0.0},\"publish_in\":0,\"publish_in_details\":{\"rate\":0.0},\"publish_out\":0,\"publish_out_details\":{\"rate\":0.0},\"ack\":224,\"ack_details\":{\"rate\":0.0},\"deliver_get\":224,\"deliver_get_details\":{\"rate\":0.0},\"confirm\":0,\"confirm_details\":{\"rate\":0.0},\"return_unroutable\":0,\"return_unroutable_details\":{\"rate\":0.0},\"redeliver\":0,\"redeliver_details\":{\"rate\":0.0}},\"reductions\":2477138,\"reductions_details\":{\"rate\":0.0},\"messages\":100,\"messages_details\":{\"rate\":0.0},\"messages_ready\":0,\"messages_ready_details\":{\"rate\":0.0},\"messages_unacknowledged\":0,\"messages_unacknowledged_details\":{\"rate\":0.0},\"idle_since\":\"2018-06-08 7:48:24\",\"consumer_utilisation\":null,\"policy\":null,\"exclusive_consumer_tag\":null,\"consumers\":2,\"recoverable_slaves\":null,\"state\":\"running\",\"garbage_collection\":{\"max_heap_size\":0,\"min_bin_vheap_size\":46422,\"min_heap_size\":233,\"fullsweep_after\":65535,\"minor_gcs\":2},\"messages_ram\":0,\"messages_ready_ram\":0,\"messages_unacknowledged_ram\":0,\"messages_persistent\":0,\"message_bytes\":0,\"message_bytes_ready\":0,\"message_bytes_unacknowledged\":0,\"message_bytes_ram\":0,\"message_bytes_persistent\":0,\"head_message_timestamp\":null,\"disk_reads\":0,\"disk_writes\":224,\"backing_queue_status\":{\"mode\":\"default\",\"q1\":0,\"q2\":0,\"delta\":[\"delta\",\"undefined\",0,\"undefined\"],\"q3\":0,\"q4\":0,\"len\":0,\"target_ram_count\":\"infinity\",\"next_seq_id\":224,\"avg_ingress_rate\":0.0,\"avg_egress_rate\":0.0,\"avg_ack_ingress_rate\":0.0,\"avg_ack_egress_rate\":0.0},\"node\":\"rabbit@c3mq01test\",\"arguments\":{},\"exclusive\":false,\"auto_delete\":false,\"durable\":true,\"vhost\":\"/\",\"name\":\"gateway_statistics\"}]";
        OkHttpUtil.OkResponse response = new OkHttpUtil.OkResponse();
        response.setCode(200);
        response.setResult(str);
        when(OkHttpUtil.doGet(eq(item.getUri()), eq(""), Mockito.anyMap())).thenReturn(response);
        Method method = RabbitMqMonitorJob.class.getDeclaredMethod("mqSumInfo", BaseServiceMonitorItem.class);
        method.setAccessible(true);
        RabbitMqMonitorJob job = new RabbitMqMonitorJob();
        Map<String, Integer> rs = (Map<String, Integer>) method.invoke(job, item);
        log.info(JSON.toJSONString(rs));
        Assert.assertNotNull(rs);
        Assert.assertEquals(rs.size(), 1);
    }

} 

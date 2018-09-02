package com.cukamartin.microservicestracing.model;

import com.cukamartin.microservicestracing.model.enums.InvocationDirection;
import com.cukamartin.microservicestracing.model.enums.InvocationProtocol;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

@Document(indexName = "audit-2018.135", type = "audit")
public class Trace {

    @Id
    private String id;

    @Field(type = FieldType.Text)
    private String correlationId;
    @Field(type = FieldType.Keyword)
    private InvocationProtocol protocol;
    @Field(type = FieldType.Keyword)
    private InvocationDirection direction;
    @Field(type = FieldType.Text)
    private String user;
    @Field(type = FieldType.Date)
    private Date startDate;
    @Field(type = FieldType.Long)
    private Long duration;
    @Field(type = FieldType.Text)
    private String uri;
    @Field(type = FieldType.Keyword)
    private String method;
    @Field(type = FieldType.Text)
    private String namespace;
    @Field(type = FieldType.Keyword)
    private String operation;
    @Field(type = FieldType.Integer)
    private int statusCode;
    @Field(type = FieldType.Keyword)
    private String topic;
    @Field(type = FieldType.Text)
    private Object request;
    @Field(type = FieldType.Text)
    private Object response;
    @Field(type = FieldType.Text)
    private String msguid;
    @Field(type = FieldType.Text)
    private String cidla;
    @Field(type = FieldType.Text)
    private String taskId;
    @Field(type = FieldType.Text)
    private String nodeId;
    @Field(type = FieldType.Text)
    private String externalSystem;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public InvocationProtocol getProtocol() {
        return protocol;
    }

    public void setProtocol(InvocationProtocol protocol) {
        this.protocol = protocol;
    }

    public InvocationDirection getDirection() {
        return direction;
    }

    public void setDirection(InvocationDirection direction) {
        this.direction = direction;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Object getRequest() {
        return request;
    }

    public void setRequest(Object request) {
        this.request = request;
    }

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }

    public String getMsguid() {
        return msguid;
    }

    public void setMsguid(String msguid) {
        this.msguid = msguid;
    }

    public String getCidla() {
        return cidla;
    }

    public void setCidla(String cidla) {
        this.cidla = cidla;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getExternalSystem() {
        return externalSystem;
    }

    public void setExternalSystem(String externalSystem) {
        this.externalSystem = externalSystem;
    }
}

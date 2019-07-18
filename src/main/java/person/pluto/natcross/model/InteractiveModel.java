package person.pluto.natcross.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import person.pluto.natcross.common.CommonFormat;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InteractiveModel {

    public static InteractiveModel of(InteractiveTypeEnum interactiveTypeEnum, String key, String value) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(key, value);

        return new InteractiveModel(CommonFormat.getInteractiveSeq(), interactiveTypeEnum.name(), jsonObject);
    }

    public static InteractiveModel of(String interactiveSeq, InteractiveTypeEnum interactiveTypeEnum, Object data) {
        return new InteractiveModel(interactiveSeq, interactiveTypeEnum.name(),
                JSON.parseObject(JSON.toJSONString(data)));
    }

    public static InteractiveModel of(InteractiveTypeEnum interactiveTypeEnum, Object data) {
        return new InteractiveModel(CommonFormat.getInteractiveSeq(), interactiveTypeEnum.name(),
                JSON.parseObject(JSON.toJSONString(data)));
    }

    public static InteractiveModel of(String interactiveType, Object data) {
        return new InteractiveModel(CommonFormat.getInteractiveSeq(), interactiveType,
                JSON.parseObject(JSON.toJSONString(data)));
    }

    private String interactiveSeq;
    private String interactiveType;
    private JSONObject data;

    public String toJSONString() {
        return JSON.toJSONString(this);
    }

}

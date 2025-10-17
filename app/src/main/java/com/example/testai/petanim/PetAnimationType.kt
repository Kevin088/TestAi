package com.example.testai.petanim

/**
 * 仙宠动画类型枚举
 * 定义了各种动画场景类型及其对应的优先级
 */
enum class PetAnimationType(val typeId: Int, val priority: Int, val description: String) {
    
    // 优先级1：弹窗类
    POPUP_FLOAT(1, 1, "Popup Float"),
    
    // 优先级2：PK类
    PK_ATTACK(8, 2, "PK Attack"),
    PK_TRAJECTORY(9, 2, "PK Trajectory"),
    PK_VICTORY(10, 2, "PK Victory"),
    PK_FAIL(11, 2, "PK Fail"),
    
    // 优先级3：飘屏
    FLOAT_SCREEN(12, 3, "Float Screen"),
    
    // 优先级4：位移（中大礼物、贵族、进场欢迎）
    MEDIUM_LARGE_GIFT(4, 4, "Medium Large Gift"),
    OPEN_NOBLE(5, 4, "Open Noble"),
    ENTER_WELCOME(7, 4, "Enter Welcome"),
    ADD_GUARD(13, 4, "Add Guard"),
    
    // 优先级5：固定位置动效（小礼物、上墙）
    SMALL_GIFT(3, 5, "Small Gift"),
    WALL_UP(6, 5, "Wall Up"),
    HOURLY_RANK(14, 5, "Hourly Rank"),
    
    // 优先级6：默认状态动效
    NORMAL_STYLE(2, 6, "Normal Style");
    
    /**
     * 判断是否与另一个动画类型相等
     * @param other 另一个动画类型
     * @return 是否相等
     */
    fun isEqualTo(other: PetAnimationType?): Boolean {
        return this == other
    }
    
    /**
     * 判断是否与指定类型ID相等
     * @param typeId 类型ID
     * @return 是否相等
     */
    fun isEqualTo(typeId: Int): Boolean {
        return this.typeId == typeId
    }
    
    /**
     * 判断是否与指定描述相等
     * @param description 描述
     * @return 是否相等
     */
    fun isEqualTo(description: String): Boolean {
        return this.description == description
    }
    
    /**
     * 判断是否比另一个动画类型优先级更高
     * @param other 另一个动画类型
     * @return 是否优先级更高
     */
    fun hasHigherPriorityThan(other: PetAnimationType): Boolean {
        return this.priority < other.priority
    }
    
    /**
     * 判断是否比另一个动画类型优先级更低
     * @param other 另一个动画类型
     * @return 是否优先级更低
     */
    fun hasLowerPriorityThan(other: PetAnimationType): Boolean {
        return this.priority > other.priority
    }
    
    /**
     * 判断是否与另一个动画类型优先级相等
     * @param other 另一个动画类型
     * @return 是否优先级相等
     */
    fun hasSamePriorityAs(other: PetAnimationType): Boolean {
        return this.priority == other.priority
    }
    
    companion object {
        /**
         * 根据类型ID获取动画类型
         */
        fun getByTypeId(typeId: Int): PetAnimationType? {
            return values().find { it.typeId == typeId }
        }
        
        /**
         * 对比两个动画类型是否相等
         * @param type1 第一个动画类型
         * @param type2 第二个动画类型
         * @return 是否相等
         */
        fun areEqual(type1: PetAnimationType?, type2: PetAnimationType?): Boolean {
            return type1 == type2
        }
        
        /**
         * 对比两个动画类型的优先级
         * @param type1 第一个动画类型
         * @param type2 第二个动画类型
         * @return 优先级对比结果 (-1: type1优先级高, 0: 相等, 1: type2优先级高)
         */
        fun comparePriority(type1: PetAnimationType, type2: PetAnimationType): Int {
            return type1.priority.compareTo(type2.priority)
        }
        
        /**
         * 检查第一个动画类型是否比第二个优先级更高
         * @param type1 第一个动画类型
         * @param type2 第二个动画类型
         * @return type1是否优先级更高（数字越小优先级越高）
         */
        fun hasHigherPriority(type1: PetAnimationType, type2: PetAnimationType): Boolean {
            return type1.priority < type2.priority
        }
    }
}
/*
 * Decompiled with CFR 0.149.
 */
package cn.Judgment.util.utilities.vector.impl;

import cn.Judgment.util.utilities.vector.Vector;

public class Vector2<T extends Number>
extends Vector<Number> {
    public Vector2(T x, T y) {
        super(x, y, 0);
    }

    public Vector3<T> toVector3() {
        return new Vector3(this.getX(), this.getY(), this.getZ());
    }
}


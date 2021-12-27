package com.github.ferusm.assignment.jetbrains.serializer

import com.github.ferusm.assignment.jetbrains.role.Role
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object RoleSerializer : KSerializer<Role> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Role", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Role {
        val roleName = decoder.decodeString()
        return Role.get(roleName)
    }

    override fun serialize(encoder: Encoder, value: Role) {
        val roleName = value.name
        encoder.encodeString(roleName)
    }
}
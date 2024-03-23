package by.bashlikovvv.core.base

interface IMapper<Entity, Domain> {

    fun mapFromEntity(entity: Entity): Domain

    fun mapToEntity(domain: Domain): Entity

}
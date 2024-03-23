package by.bashlikovvv.core.domain.usecase

import by.bashlikovvv.core.domain.model.Location
import by.bashlikovvv.core.domain.repository.ILocationsRepository

class AddLocationUseCase(private val locationsRepository: ILocationsRepository) {

    suspend fun execute(location: Location) = locationsRepository.addLocation(location)

}
package by.bashlikovvv.core.domain.usecase

import by.bashlikovvv.core.domain.model.Location
import by.bashlikovvv.core.domain.repository.ILocationsRepository

class UpdateLocationUseCase(private val locationsRepository: ILocationsRepository) {

    suspend fun execute(id: Int, modifier: suspend (Location) -> Location) {
        locationsRepository.updateLocation(id + 1, modifier)
    }

}
package by.bashlikovvv.core.domain.usecase

import by.bashlikovvv.core.domain.repository.ILocationsRepository

class UpdateSectionNameUseCase(private val locationsRepository: ILocationsRepository) {

    suspend fun execute(newName: String) = locationsRepository.updateSection(newName)

}
package ma.apostorial.tmdl_backend.record.dtos.classic;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record ClassicRecordUpdateRequest(
    @Min(value = 1, message = "Record percentage must be at least 1")
    @Max(value = 100, message = "Record percentage cannot exceed 100")
    int recordPercentage,
    @Size(max = 255)
    String videoLink
) { }

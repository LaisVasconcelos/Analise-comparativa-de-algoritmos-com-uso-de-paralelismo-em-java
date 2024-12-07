__kernel void countOccurrences(
    __global const char *content,
    __global const char *targetWord,
    const int targetLength,
    __global int *result)
{
    int gid = get_global_id(0);
    int match = 1;

    for (int i = 0; i < targetLength; i++) {
        if (content[gid + i] != targetWord[i] || gid + i >= get_global_size(0)) {
            match = 0;
            break;
        }
    }

    result[gid] = match;
}
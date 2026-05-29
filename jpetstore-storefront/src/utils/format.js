export function stripHtml(html) {
  if (!html) return ''
  return html.replace(/<[^>]*>/g, '').trim()
}

export function extractImageSrc(html) {
  if (!html) return ''
  const match = html.match(/<image\s+src=["']([^"']*)["']/i)
  return match ? match[1].replace('../images/', '/images/') : ''
}
